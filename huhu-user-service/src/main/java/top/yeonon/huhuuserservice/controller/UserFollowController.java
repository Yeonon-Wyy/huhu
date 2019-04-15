package top.yeonon.huhuuserservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
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
    public UserFollowResponseVo follow(@PathVariable("id") Long id,
                                       @RequestBody UserFollowRequestVo request) throws HuhuException {
        request.setUserId(id);
        return userFollowService.follow(request);
    }

    @DeleteMapping("/{id}")
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
