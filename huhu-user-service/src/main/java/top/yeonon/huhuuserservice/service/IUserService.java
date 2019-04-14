package top.yeonon.huhuuserservice.service;

import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuuserservice.vo.request.*;
import top.yeonon.huhuuserservice.vo.response.*;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:12
 **/
public interface IUserService {

    /**
     * 用户注册接口
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常，由统一异常处理机制处理
     */
    UserRegisterResponseVo register(UserRegisterRequestVo request)
            throws HuhuException;

    /**
     * 查询用户信息接口
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    UserQueryResponseVo queryUserInfo(UserQueryRequestVo request)
        throws HuhuException;

    /**
     * 更新用户信息接口
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    UserUpdateResponseVo updateUserInfo(UserUpdateRequestVo request)
        throws HuhuException;

    /**
     * 删除用户接口，该接口的实现应该仅仅将用户状态置位CLOSE(-1)，并不删除数据库记录
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    UserDeleteResponseVo deleteUser(UserDeleteRequestVo request)
        throws HuhuException;

    /**
     * 用户登录接口
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    UserLoginResponseVo login(UserLoginRequestVo request)
        throws HuhuException;



}
