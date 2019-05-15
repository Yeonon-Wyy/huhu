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
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuuserservice.client.HuhuMailClient;
import top.yeonon.huhuuserservice.client.vo.RemoteForgetPassRequestVo;
import top.yeonon.huhuuserservice.constants.Const;
import top.yeonon.huhuuserservice.constants.ErrMessage;
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
    public UserRegisterResponseVo register(UserRegisterRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new HuhuException(ErrMessage.EXIST_SAME_USERNAME);
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
    public UserQueryResponseVo queryUserInfo(UserQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ErrMessage.NOT_FOUND_USER);
        }

        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ErrMessage.ALREADY_CLOSE_USER);
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
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }


        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ErrMessage.NOT_FOUND_USER);
        }

        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ErrMessage.ALREADY_CLOSE_USER);
        }

        user = userRepository.save(request.updateUser(user));
        return new UserUpdateResponseVo(user.getId());
    }

    @Override
    public UserDeleteResponseVo deleteUser(UserDeleteRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }


        User user = userRepository.findById(request.getId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ErrMessage.NOT_FOUND_USER);
        }

        //如果已经是处于注销状态，那么就不需要再次注销了
        if (user.getStatus().equals(UserStatus.CLOSE.getCode())) {
            throw new HuhuException(ErrMessage.ALREADY_CLOSE_USER);
        }

        //仅仅修改状态，并不真正删除数据记录
        user.setStatus(UserStatus.CLOSE.getCode());
        user = userRepository.save(user);

        return new UserDeleteResponseVo(user.getId());
    }



    @Override
    public UserBatchQueryResponseVo batchQueryUserInfo(UserBatchQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
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
                request.getPageSize(),
                userList.hasNext(),
                userList.hasPrevious(),
                userList.isFirst(),
                userList.isLast()
        );
    }

    @Override
    public void forgetPass(ForgetPassRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }
        if (!userRepository.existsByUsernameAndEmail(request.getUsername(), request.getEmail())) {
            throw new HuhuException(ErrMessage.USERNAME_NOT_MATCH_EMAIL);
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


    private void checkValidateCode(String username, String validatCode) throws HuhuException {


        String redisValidateCode = (String) redisTemplate.opsForValue().get(
                Const.RedisConst.FORGET_PASSWORD_VALIDATE_CODE_PREFIX + username
        );

        if (!validatCode.equals(redisValidateCode)) {
            throw new HuhuException(ErrMessage.VALIDATE_CODE_ERROR);
        }

    }

    @Override
    @Transactional
    public void updatePassword(UpdatePassRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        //检查验证码是否正确
        checkValidateCode(request.getUsername(), request.getValidateCode());

        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new HuhuException(ErrMessage.NOT_FOUND_USER);
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
    @Transactional
    public UploadAvatarResponseVo uploadAvatar(UploadAvatarRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }
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
    public BriefUserQueryResponseVo queryBriefUserInfo(BriefUserQueryRequestVo request) throws HuhuException {
        if (!request.validate()) {
            throw new HuhuException(ErrMessage.REQUEST_PARAM_ERROR);
        }

        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            throw new HuhuException(ErrMessage.NOT_FOUND_USER);
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
