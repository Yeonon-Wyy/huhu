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

    /**
     * 远程调用邮件服务来发送“重置密码”的邮件，邮件里包含验证码
     * @param remoteForgetPassRequestVo 请求对象
     * @return 响应对象（因为有统一拦截器来拦截了，所以不需要在本服务内部创建重复的对象）
     */
    @PostMapping("/mail/send/forget_password")
    ServerResponse forgetPassword(@RequestBody RemoteForgetPassRequestVo remoteForgetPassRequestVo);
}
