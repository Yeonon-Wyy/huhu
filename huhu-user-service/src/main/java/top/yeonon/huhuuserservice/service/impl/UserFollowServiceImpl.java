package top.yeonon.huhuuserservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuuserservice.constants.ErrMessage;
import top.yeonon.huhuuserservice.constants.UserStatus;
import top.yeonon.huhuuserservice.entity.User;
import top.yeonon.huhuuserservice.entity.UserFollower;
import top.yeonon.huhuuserservice.entity.UserFollowing;
import top.yeonon.huhuuserservice.repository.UserFollowerRepository;
import top.yeonon.huhuuserservice.repository.UserFollowingRepository;
import top.yeonon.huhuuserservice.repository.UserRepository;
import top.yeonon.huhuuserservice.service.IUserFollowService;
import top.yeonon.huhuuserservice.vo.request.FollowerQueryRequestVo;
import top.yeonon.huhuuserservice.vo.request.FollowingQueryRequestVo;
import top.yeonon.huhuuserservice.vo.request.UserFollowRequestVo;
import top.yeonon.huhuuserservice.vo.request.UserUnFollowRequestVo;
import top.yeonon.huhuuserservice.vo.response.FollowerQueryResponseVo;
import top.yeonon.huhuuserservice.vo.response.FollowingQueryResponseVo;
import top.yeonon.huhuuserservice.vo.response.UserFollowResponseVo;
import top.yeonon.huhuuserservice.vo.response.UserUnFollowResponseVo;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:34
 **/
@Service
public class UserFollowServiceImpl implements IUserFollowService {

    private final UserRepository userRepository;

    private final UserFollowerRepository userFollowerRepository;

    private final UserFollowingRepository userFollowingRepository;

    @Autowired
    public UserFollowServiceImpl(UserRepository userRepository,
                                 UserFollowerRepository userFollowerRepository,
                                 UserFollowingRepository userFollowingRepository) {
        this.userRepository = userRepository;
        this.userFollowerRepository = userFollowerRepository;
        this.userFollowingRepository = userFollowingRepository;
    }

    @Override
    public UserFollowResponseVo follow(UserFollowRequestVo request) throws HuhuException {

        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }


        if (request.getFollowUserId().equals(request.getUserId())) {
            throw new HuhuException(ErrMessage.NOT_ALLOW_FOLLOW_YOURSELF);
        }

        //获取发起关注的用户对象
        User followUser = userRepository.findById(request.getUserId()).orElse(null);
        //获取被关注的用户对象
        User followedUser = userRepository.findById(request.getFollowUserId()).orElse(null);
        //还需要检查对方是否已经注销
        if (followUser == null
                || followedUser == null
                || followedUser.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ErrMessage.NOT_FOUND_USER);
        }

        //0. 先检查是否已经存在关注关系
        if (checkFollowRelation(followUser.getId(), followedUser.getId())) {
            throw new HuhuException(ErrMessage.EXIST_FOLLOW_RELATION);
        }

        //1. 插入数据对user->follower 关联表中
        userFollowerRepository.save(
                new UserFollower(followedUser.getId(), followUser.getId())
        );

        //2. 插入数据到user->following 关联表中
        userFollowingRepository.save(
                new UserFollowing(followUser.getId(), followedUser.getId())
        );

        //3. 更新两个用户的follower_count和following字段
        //发起关注的用户给followingCount+1，即多关注了一个用户
        followUser.setFollowingCount(followUser.getFollowingCount() + 1);
        //被关注的用户给followerCount+1，即多了一个关注者
        followedUser.setFollowerCount(followedUser.getFollowerCount() + 1);

        //4. 更新数据
        userRepository.save(followedUser);
        userRepository.save(followUser);

        return new UserFollowResponseVo(followUser.getId());
    }

    @Override
    @Transactional
    public UserUnFollowResponseVo unFollow(UserUnFollowRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }



        //检查是否是自己关注自己
        if (request.getUnFollowId().equals(request.getUserId())) {
            throw new HuhuException(ErrMessage.NOT_ALLOW_UN_FOLLOW_YOURSELF);
        }

        //检查是否存在关注关系
        if (!checkFollowRelation(request.getUserId(), request.getUnFollowId())) {
            throw new HuhuException(ErrMessage.NOT_EXIST_FOLLOW_RELATION);
        }

        //获取发起取消关注的用户对象
        User followUser = userRepository.findById(request.getUserId()).orElse(null);
        //获取被取消关注的用户对象
        User unFollowedUser = userRepository.findById(request.getUnFollowId()).orElse(null);
        if (followUser == null || unFollowedUser == null) {
            throw new HuhuException(ErrMessage.NOT_FOUND_USER);
        }

        //1. 删除“关注我的人”表中的记录
        userFollowerRepository.deleteByUserIdAndFollowerId(unFollowedUser.getId(), followUser.getId());
        //2. 删除“我关注的人”表中的记录
        userFollowingRepository.deleteByUserIdAndFollowingId(followUser.getId(), unFollowedUser.getId());

        //更新用户关注者数量和关注的人数量
        followUser.setFollowingCount(followUser.getFollowingCount() - 1);
        unFollowedUser.setFollowerCount(unFollowedUser.getFollowerCount() - 1);
        return new UserUnFollowResponseVo(followUser.getId());
    }

    @Override
    public FollowerQueryResponseVo queryFollower(FollowerQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        //这里不检查传入的用户ID是否存在了，因为如果没有任何关注者，就不会存在记录，返回的内容也应该是空
        List<UserFollower> userFollowers = userFollowerRepository.findByUserId(request.getId());
        List<Long> followerIds = new LinkedList<>();
        userFollowers.forEach(userFollower -> {
            followerIds.add(userFollower.getFollowerId());
        });
        return new FollowerQueryResponseVo(followerIds);
    }

    @Override
    public FollowingQueryResponseVo queryFollowing(FollowingQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        List<UserFollowing> userFollowings = userFollowingRepository.findByUserId(request.getId());
        List<Long> followingIds = new LinkedList<>();
        userFollowings.forEach(userFollowing -> {
            followingIds.add(userFollowing.getFollowingId());
        });

        return new FollowingQueryResponseVo(followingIds);
    }

    /**
     * 检查双方的关注关系
     * @param followUserId 发起关注的用户ID
     * @param followedUserId 被关注的用户ID
     */
    private boolean checkFollowRelation(Long followUserId, Long followedUserId) throws HuhuException {
        //检查“关注我的人”表中是否已经存在记录
        if (userFollowerRepository.existsByUserIdAndFollowerId(followedUserId, followUserId)) {
            return true;
        }
        //检查“我关注的人”表中是否已经存在记录
        if (userFollowingRepository.existsByUserIdAndFollowingId(followUserId, followedUserId)) {
            return true;
        }

        return false;
    }
}
