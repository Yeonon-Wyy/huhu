package top.yeonon.huhuuserservice.service;

import org.springframework.web.multipart.MultipartFile;
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
     * 批量获取用户信息（隐藏私密信息）
     * @param request 请求对象
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    UserBatchQueryResponseVo batchQueryUserInfo(UserBatchQueryRequestVo request)
        throws HuhuException;


    //以下三个方法是修改密码的流程
    //1. 发起“忘记密码”请求，需要提供用户名和邮箱（或者手机），系统生成验证码，通过邮箱或者手机发送给用户
    //2. 用户拿到验证码，再次发起请求，此时前端应该持有用户名，故不需要用户再次提供，由前端填充即可，验证码需要用户提供
    //3. 验证通过后，用户输入新密码，然后更新用户信息即可，如果用户此时处于登录状态，则另其重新登录


    /**
     * 忘记密码服务
     * @param request 请求对象
     * @throws HuhuException 可能抛出的异常
     */
    void forgetPass(ForgetPassRequestVo request) throws HuhuException;


    /**
     * 更新密码
     * @param request 请求对象（包含上一个步骤中得到的验证码）
     * @throws HuhuException 可能抛出的异常
     */
    void updatePassword(UpdatePassRequestVo request) throws HuhuException;

    /**
     * 上传头像
     * @param requestVo 包含头像二进制文件
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    UploadAvatarResponseVo uploadAvatar(UploadAvatarRequestVo requestVo) throws HuhuException;

    /**
     * 获取用户简要信息（即可以公开的信息）
     * @param request 请求（用户ID）
     * @return 响应对象
     * @throws HuhuException 可能抛出的异常
     */
    BriefUserQueryResponseVo queryBriefUserInfo(BriefUserQueryRequestVo request) throws HuhuException;
}
