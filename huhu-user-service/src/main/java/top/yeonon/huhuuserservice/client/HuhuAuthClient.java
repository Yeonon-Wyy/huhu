package top.yeonon.huhuuserservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 17:37
 **/
@FeignClient(value = "huhu-auth-server")
@Component
public interface HuhuAuthClient {

    String AUTH_TOKEN = "Authorization";

    /**
     * 调用生产者服务，Open-Feign需要自己传递token，不过可以写一个OpenFeign拦截器来实现，两种方法皆可，自行选择
     * @param token JWT
     */
    @DeleteMapping(value = "/oath/users/logout")
    void logout(@RequestHeader(AUTH_TOKEN) String token);
}
