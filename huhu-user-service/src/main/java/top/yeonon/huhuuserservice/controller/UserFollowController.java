package top.yeonon.huhuuserservice.controller;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuuserservice.constants.ErrorMsg;
import top.yeonon.huhuuserservice.interceptor.annotation.CheckId;
import top.yeonon.huhuuserservice.service.IUserFollowService;
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
 * @date 2019/4/14 0014 17:19
 **/
@RestController
@RequestMapping("/users/follow/")
public class UserFollowController {

    private final IUserFollowService userFollowService;

    @Autowired
    public UserFollowController(IUserFollowService userFollowService) {
        this.userFollowService = userFollowService;
    }

    @PostMapping("/{id}")
    @CheckId
    public UserFollowResponseVo follow(@PathVariable("id") Long id,
                                       @RequestBody UserFollowRequestVo request) throws HuhuException {

        request.setUserId(id);
        return userFollowService.follow(request);
    }

    @DeleteMapping("/{id}")
    @CheckId
    public UserUnFollowResponseVo unFollow(@PathVariable("id") Long id,
                                           @RequestBody UserUnFollowRequestVo request) throws HuhuException {
        request.setUserId(id);

        return userFollowService.unFollow(request);
    }

    @GetMapping("/follower/{id}")
    public FollowerQueryResponseVo queryFollower(@PathVariable("id") Long id) throws HuhuException {
        FollowerQueryRequestVo request = new FollowerQueryRequestVo(id);
        return userFollowService.queryFollower(request);
    }



    @GetMapping("/following/{id}")
    public FollowingQueryResponseVo queryFollowing(@PathVariable("id") Long id) throws HuhuException {

        FollowingQueryRequestVo request = new FollowingQueryRequestVo(id);
        return userFollowService.queryFollowing(request);
    }
}
