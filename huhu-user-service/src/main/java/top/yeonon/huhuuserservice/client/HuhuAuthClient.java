package top.yeonon.huhuuserservice.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 17:37
 **/
@FeignClient(value = "huhu-auth-server")
@Component
public interface HuhuAuthClient {

    String AUTH_TOKEN = "Authorization";

    //用open-feign消费服务的时候，需要传递token header
    @DeleteMapping(value = "/oath/users/logout")
    void logout(@RequestHeader(AUTH_TOKEN) String token);
}
