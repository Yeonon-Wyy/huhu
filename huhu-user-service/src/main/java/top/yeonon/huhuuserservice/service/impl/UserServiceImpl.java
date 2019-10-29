package top.yeonon.huhuuserservice.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yeonon.huhucommon.aop.ParamValidate;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.response.ResponseCode;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuuserservice.client.HuhuMailClient;
import top.yeonon.huhuuserservice.client.vo.RemoteForgetPassRequestVo;
import top.yeonon.huhuuserservice.constants.Const;
import top.yeonon.huhuuserservice.constants.UserStatus;
import top.yeonon.huhuuserservice.entity.User;
import top.yeonon.huhuuserservice.properties.HuhuFtpProperties;
import top.yeonon.huhuuserservice.repository.UserRepository;
import top.yeonon.huhuuserservice.service.IUserService;
import top.yeonon.huhuuserservice.vo.request.*;
import top.yeonon.huhuuserservice.vo.response.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:12
 **/
@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<Object, Object> redisTemplate;

    private final HuhuMailClient huhuMailClient;

    private final HuhuFtpProperties ftpProperties;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RedisTemplate<Object, Object> redisTemplate, HuhuMailClient huhuMailClient, HuhuFtpProperties ftpProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.huhuMailClient = huhuMailClient;
        this.ftpProperties = ftpProperties;
    }

    @Override
    @ParamValidate
    public UserRegisterResponseVo register(UserRegisterRequestVo request) throws HuhuException {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new HuhuException(ResponseCode.EXIST_SAME_USERNAME.getCode(),
                    ResponseCode.EXIST_SAME_USERNAME.getDescription());
        }


        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
        user = userRepository.save(user);
        return new UserRegisterResponseVo(user.getId());
    }



    @Override
    @ParamValidate
    public UserQueryResponseVo queryUserInfo(UserQueryRequestVo request) throws HuhuException {

        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ResponseCode.NOT_FOUND_USER.getCode(),
                    ResponseCode.NOT_FOUND_USER.getDescription());
        }

        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ResponseCode.ALREADY_CLOSE_USER.getCode(),
                    ResponseCode.ALREADY_CLOSE_USER.getDescription());
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
    @ParamValidate
    public UserUpdateResponseVo updateUserInfo(UserUpdateRequestVo request) throws HuhuException {

        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ResponseCode.NOT_FOUND_USER.getCode(),
                    ResponseCode.NOT_FOUND_USER.getDescription());
        }

        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ResponseCode.ALREADY_CLOSE_USER.getCode(),
                    ResponseCode.NOT_FOUND_USER.getDescription());
        }

        user = userRepository.save(request.updateUser(user));
        return new UserUpdateResponseVo(user.getId());
    }

    @Override
    @ParamValidate
    public UserDeleteResponseVo deleteUser(UserDeleteRequestVo request) throws HuhuException {

        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ResponseCode.NOT_FOUND_USER.getCode(),
                    ResponseCode.NOT_FOUND_USER.getDescription());
        }

        //如果已经是处于注销状态，那么就不需要再次注销了
        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ResponseCode.ALREADY_CLOSE_USER.getCode(),
                    ResponseCode.ALREADY_CLOSE_USER.getDescription());
        }

        //仅仅修改状态，并不真正删除数据记录
        user.setStatus(UserStatus.CLOSE.getCode());
        user = userRepository.save(user);

        return new UserDeleteResponseVo(user.getId());
    }



    @Override
    @ParamValidate
    public UserBatchQueryResponseVo batchQueryUserInfo(UserBatchQueryRequestVo request) throws HuhuException {

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
                request.getPageSize(),
                userList.hasNext(),
                userList.hasPrevious(),
                userList.isFirst(),
                userList.isLast()
        );
    }

    @Override
    @ParamValidate
    public void forgetPass(ForgetPassRequestVo request) throws HuhuException {

        if (!userRepository.existsByUsernameAndEmail(request.getUsername(), request.getEmail())) {
            throw new HuhuException(ResponseCode.USERNAME_NOT_MATCH_EMAIL.getCode(),
                    ResponseCode.USERNAME_NOT_MATCH_EMAIL.getDescription());
        }

        //存入redis
        String validateCode = CommonUtils.generateValidateCode(Const.RedisConst.FORGET_PASSWORD_VALIDATE_CODE_COUNT);
        redisTemplate.opsForValue().set(
                Const.RedisConst.FORGET_PASSWORD_VALIDATE_CODE_PREFIX + request.getUsername(),
                validateCode,
                Const.RedisConst.FORGET_PASSWORD_VALIDATE_CODE_TIMEOUT,
                TimeUnit.SECONDS
        );

        RemoteForgetPassRequestVo remoteRequest = new RemoteForgetPassRequestVo();
        remoteRequest.setTo(request.getEmail());
        Map<String, Object> content = Maps.newHashMap();
        content.put("username", request.getUsername());
        content.put("validateCode", validateCode);
        remoteRequest.setContent(content);
        //发送邮件
        huhuMailClient.forgetPassword(remoteRequest);

    }

    @ParamValidate
    private void checkValidateCode(String username, String validateCode) throws HuhuException {

        String redisValidateCode = (String) redisTemplate.opsForValue().get(
                Const.RedisConst.FORGET_PASSWORD_VALIDATE_CODE_PREFIX + username
        );

        if (!validateCode.equals(redisValidateCode)) {
            throw new HuhuException(ResponseCode.VALIDATE_CODE_ERROR.getCode(),
                    ResponseCode.VALIDATE_CODE_ERROR.getDescription());
        }

    }

    @Override
    @ParamValidate
    @Transactional
    public void updatePassword(UpdatePassRequestVo request) throws HuhuException {

        //检查验证码是否正确
        checkValidateCode(request.getUsername(), request.getValidateCode());

        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new HuhuException(ResponseCode.NOT_FOUND_USER.getCode(),
                    ResponseCode.NOT_FOUND_USER.getDescription());
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        //更新密码到数据库
        userRepository.save(user);

        //从Redis里删除
        redisTemplate.delete(Const.RedisConst.FORGET_PASSWORD_VALIDATE_CODE_PREFIX + request.getUsername());
    }

    @Override
    @ParamValidate
    @Transactional
    public UploadAvatarResponseVo uploadAvatar(UploadAvatarRequestVo request) throws HuhuException {

        String avatarFileName = CommonUtils.uploadFile(
                ftpProperties.getHost(),
                ftpProperties.getPort(),
                ftpProperties.getUsername(),
                ftpProperties.getPassword(),
                Const.UserConst.AVATAR_DIR,
                request.getFile()
        );

        String filePath = ftpProperties.getServerAddr() + avatarFileName;


        userRepository.updateAvatarById(filePath, request.getUserId());
        return new UploadAvatarResponseVo(filePath);
    }

    @Override
    @ParamValidate
    public BriefUserQueryResponseVo queryBriefUserInfo(BriefUserQueryRequestVo request) throws HuhuException {

        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ResponseCode.NOT_FOUND_USER.getCode(),
                    ResponseCode.NOT_FOUND_USER.getDescription());
        }

        BriefUserQueryResponseVo.BriefUserInfo briefUserInfo = new BriefUserQueryResponseVo.BriefUserInfo(
                user.getUsername(),
                user.getStatus(),
                user.getAvatar(),
                user.getSex(),
                user.getFollowerCount(),
                user.getFollowingCount(),
                user.getProfile(),
                user.getDegree()
        );

        return new BriefUserQueryResponseVo(briefUserInfo);

    }
}
