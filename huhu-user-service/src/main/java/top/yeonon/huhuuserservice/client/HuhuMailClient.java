package top.yeonon.huhuuserservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.yeonon.huhucommon.response.ServerResponse;
import top.yeonon.huhuuserservice.client.vo.RemoteForgetPassRequestVo;

/**
 * @Author yeonon
 * @date 2019/4/18 0018 16:50
 **/
@FeignClient(value = "huhu-mail-service")
@Component
public interface HuhuMailClient {

    @PostMapping("/mail/send/forget_password")
    ServerResponse forgetPassword(@RequestBody RemoteForgetPassRequestVo remoteForgetPassRequestVo);
}
