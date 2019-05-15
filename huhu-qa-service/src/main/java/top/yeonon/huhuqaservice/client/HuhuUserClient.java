package top.yeonon.huhuqaservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.yeonon.huhucommon.response.ServerResponse;

/**
 * @Author yeonon
 * @date 2019/5/15 0015 12:56
 **/
@FeignClient(name = "huhu-user-service")
@Component
public interface HuhuUserClient {

    @GetMapping("/users/brief/{id}")
    ServerResponse queryBriefUserInfo(@PathVariable("id") Long id);
}
