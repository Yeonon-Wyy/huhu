package top.yeonon.huhuuserservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuuserservice.service.IUserService;
import top.yeonon.huhuuserservice.vo.request.*;
import top.yeonon.huhuuserservice.vo.response.*;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:49
 **/
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserRegisterResponseVo register(@RequestBody UserRegisterRequestVo request) throws HuhuException {
        return userService.register(request);
    }

    @GetMapping("/{id}")
    public UserQueryResponseVo queryUser(@PathVariable("id") Long id) throws HuhuException {
        UserQueryRequestVo request = new UserQueryRequestVo(id);

        return userService.queryUserInfo(request);
    }

    @PutMapping("/{id}")
    public UserUpdateResponseVo updateUser(@PathVariable("id") Long id,
                                           @RequestBody UserUpdateRequestVo request) throws HuhuException {
        request.setId(id);
        return userService.updateUserInfo(request);
    }

    @DeleteMapping("/{id}")
    public UserDeleteResponseVo deleteUser(@PathVariable("id") Long id) throws HuhuException {
        UserDeleteRequestVo request = new UserDeleteRequestVo(id);
        return userService.deleteUser(request);
    }


    @GetMapping("/batch/query")
    public UserBatchQueryResponseVo batchQueryUserInfo(@RequestBody UserBatchQueryRequestVo request) throws HuhuException {
        return userService.batchQueryUserInfo(request);
    }

    @PostMapping("/password/forget")
    public void forgetPassword(@RequestBody ForgetPassRequestVo request) throws HuhuException {
        userService.forgetPass(request);
    }

    @PutMapping("/password/update")
    public void updatePassword(@RequestBody UpdatePassRequestVo request) throws HuhuException {
        userService.updatePassword(request);
    }
}
