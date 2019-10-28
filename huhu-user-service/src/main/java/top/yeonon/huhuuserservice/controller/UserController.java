package top.yeonon.huhuuserservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuuserservice.interceptor.annotation.CheckId;
import top.yeonon.huhuuserservice.service.IUserFollowService;
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

    private final IUserFollowService userFollowService;


    @Autowired
    public UserController(IUserService userService,
                          IUserFollowService userFollowService) {
        this.userService = userService;
        this.userFollowService = userFollowService;
    }

    @PostMapping
    public UserRegisterResponseVo register(@RequestBody UserRegisterRequestVo request) throws HuhuException {
        return userService.register(request);
    }

    @GetMapping("/{id}")
    @CheckId
    public UserQueryResponseVo queryUser(@PathVariable("id") Long id) throws HuhuException {
        UserQueryRequestVo request = new UserQueryRequestVo(id);

        return userService.queryUserInfo(request);
    }

    @PutMapping("/{id}")
    @CheckId
    public UserUpdateResponseVo updateUser(@PathVariable("id") Long id,
                                           @RequestBody UserUpdateRequestVo request) throws HuhuException {

        request.setId(id);
        return userService.updateUserInfo(request);
    }

    @DeleteMapping("/{id}")
    @CheckId
    public UserDeleteResponseVo deleteUser(@PathVariable("id") Long id) throws HuhuException {


        UserDeleteRequestVo request = new UserDeleteRequestVo(id);
        return userService.deleteUser(request);
    }

    @GetMapping("brief/{id}")
    public BriefUserQueryResponseVo queryBriefUserInfo(@PathVariable("id") Long id) throws HuhuException {
        BriefUserQueryRequestVo request = new BriefUserQueryRequestVo(id);
        return userService.queryBriefUserInfo(request);
    }

    @GetMapping("/batch/query")
    public UserBatchQueryResponseVo batchQueryUserInfo(@RequestBody UserBatchQueryRequestVo request,
                                                       @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) throws HuhuException {
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
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

    @PutMapping("/{id}/avatar/upload")
    @CheckId
    public UploadAvatarResponseVo uploadAvatar(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws HuhuException {
        UploadAvatarRequestVo request = new UploadAvatarRequestVo(
                id,
                file
        );
        return userService.uploadAvatar(request);
    }


    @PostMapping("{id}/follow/{followUserId}")
    @CheckId
    public UserFollowResponseVo follow(@PathVariable("id") Long id,
                                       @PathVariable("followUserId") Long followUserId) throws HuhuException {

        UserFollowRequestVo request = new UserFollowRequestVo(
                id,
                followUserId
        );
        return userFollowService.follow(request);
    }

    @DeleteMapping("{id}/follow/{unFollowId}")
    @CheckId
    public UserUnFollowResponseVo unFollow(@PathVariable("id") Long id,
                                           @PathVariable("unFollowId") Long unFollowId) throws HuhuException {
        UserUnFollowRequestVo request = new UserUnFollowRequestVo(
                id,
                unFollowId
        );
        return userFollowService.unFollow(request);
    }

    @GetMapping("{id}/follower")
    public FollowerQueryResponseVo queryFollower(@PathVariable("id") Long id) throws HuhuException {
        FollowerQueryRequestVo request = new FollowerQueryRequestVo(id);
        return userFollowService.queryFollower(request);
    }

    @GetMapping("{id}/following")
    public FollowingQueryResponseVo queryFollowing(@PathVariable("id") Long id) throws HuhuException {

        FollowingQueryRequestVo request = new FollowingQueryRequestVo(id);
        return userFollowService.queryFollowing(request);
    }

}
