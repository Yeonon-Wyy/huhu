package top.yeonon.huhuuserservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.yeonon.huhucommon.exception.HuhuException;
import top.yeonon.huhuuserservice.service.IUserFollowService;
import top.yeonon.huhuuserservice.vo.request.UserFollowRequestVo;
import top.yeonon.huhuuserservice.vo.request.UserUnFollowRequestVo;
import top.yeonon.huhuuserservice.vo.response.UserFollowResponseVo;
import top.yeonon.huhuuserservice.vo.response.UserUnFollowResponseVo;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:19
 **/
@RestController
@RequestMapping("/follow/user")
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
}
