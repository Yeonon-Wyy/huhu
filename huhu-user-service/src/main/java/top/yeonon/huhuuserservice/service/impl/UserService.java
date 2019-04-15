package top.yeonon.huhuuserservice.service.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuuserservice.constants.ErrorMsg;
import top.yeonon.huhuuserservice.constants.UserStatus;
import top.yeonon.huhuuserservice.entity.User;
import top.yeonon.huhuuserservice.entity.UserFollower;
import top.yeonon.huhuuserservice.entity.UserFollowing;
import top.yeonon.huhuuserservice.repository.UserFollowerRepository;
import top.yeonon.huhuuserservice.repository.UserFollowingRepository;
import top.yeonon.huhuuserservice.repository.UserRepository;
import top.yeonon.huhuuserservice.service.IUserService;
import top.yeonon.huhuuserservice.vo.request.*;
import top.yeonon.huhuuserservice.vo.response.*;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:12
 **/
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;



    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public UserRegisterResponseVo register(UserRegisterRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrorMsg.REQUEST_PARAM_ERROR);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new HuhuException(ErrorMsg.EXIST_SAME_USERNAME);
        }


        User user = new User(
                request.getUsername(),
                CommonUtils.md5(request.getPassword()),
                request.getEmail()
        );
        user = userRepository.save(user);
        return new UserRegisterResponseVo(user.getId());
    }

    @Override
    public UserQueryResponseVo queryUserInfo(UserQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrorMsg.REQUEST_PARAM_ERROR);
        }

        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ErrorMsg.NOT_FOUND_USER);
        }

        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ErrorMsg.ALREADY_CLOSE_USER);
        }

        return new UserQueryResponseVo(
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getAvatar(),
                user.getStatus(),
                user.getSex(),
                user.getFollowerCount(),
                user.getFollowingCount(),
                user.getProfile(),
                user.getIndustry(),
                user.getDegree()
        );
    }

    @Override
    public UserUpdateResponseVo updateUserInfo(UserUpdateRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrorMsg.REQUEST_PARAM_ERROR);
        }

        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ErrorMsg.NOT_FOUND_USER);
        }

        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ErrorMsg.ALREADY_CLOSE_USER);
        }

        user = userRepository.save(request.updateUser(user));
        return new UserUpdateResponseVo(user.getId());
    }

    @Override
    public UserDeleteResponseVo deleteUser(UserDeleteRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrorMsg.REQUEST_PARAM_ERROR);
        }

        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ErrorMsg.NOT_FOUND_USER);
        }

        //如果已经是处于注销状态，那么就不需要再次注销了
        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ErrorMsg.ALREADY_CLOSE_USER);
        }

        //仅仅修改状态，并不真正删除数据记录
        user.setStatus(UserStatus.CLOSE.getCode());
        user = userRepository.save(user);

        return new UserDeleteResponseVo(user.getId());
    }

    //TODO: 实现了安全认证功能之后再写登录方法
    @Override
    public UserLoginResponseVo login(UserLoginRequestVo request) throws HuhuException {
        return null;
    }

    @Override
    public UserBatchQueryResponseVo batchQueryUserInfo(UserBatchQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrorMsg.REQUEST_PARAM_ERROR);
        }

        //分页查询
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Page<User> userList = userRepository.findAllByIdIn(
                request.getIds(),
                PageRequest.of(request.getPageNum(),
                request.getPageSize(), sort)
        );

        List<UserBatchQueryResponseVo.UserInfo> userInfoList = Lists.newLinkedList();
        userList.forEach(user -> {
            userInfoList.add(new UserBatchQueryResponseVo.UserInfo(
                    user.getId(),
                    user.getUsername(),
                    user.getAvatar(),
                    user.getSex(),
                    user.getAddress(),
                    user.getFollowerCount(),
                    user.getFollowingCount(),
                    user.getProfile(),
                    user.getIndustry()
            ));
        });

        return new UserBatchQueryResponseVo(
                userInfoList,
                request.getPageNum(),
                request.getPageSize()
        );
    }


}
