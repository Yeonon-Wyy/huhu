package top.yeonon.huhuuserservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuuserservice.vo.request.FollowerQueryRequestVo;
import top.yeonon.huhuuserservice.vo.request.FollowingQueryRequestVo;
import top.yeonon.huhuuserservice.vo.request.UserFollowRequestVo;
import top.yeonon.huhuuserservice.vo.request.UserUnFollowRequestVo;
import top.yeonon.huhuuserservice.vo.response.FollowerQueryResponseVo;
import top.yeonon.huhuuserservice.vo.response.FollowingQueryResponseVo;
import top.yeonon.huhuuserservice.vo.response.UserFollowResponseVo;
import top.yeonon.huhuuserservice.vo.response.UserUnFollowResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:34
 **/
public interface IUserFollowService {

    /**
     * 发起关注请求（即关注某个用户）
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    UserFollowResponseVo follow(UserFollowRequestVo request)
            throws HuhuException;


    /**
     * 取消关注
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    UserUnFollowResponseVo unFollow(UserUnFollowRequestVo request)
        throws HuhuException;


    /**
     * 获取关注我的用户的id列表
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    FollowerQueryResponseVo queryFollower(FollowerQueryRequestVo request)
        throws HuhuException;

    /**
     * 获取我关注的用户的id列表
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    FollowingQueryResponseVo queryFollowing(FollowingQueryRequestVo request)
        throws HuhuException;
}
