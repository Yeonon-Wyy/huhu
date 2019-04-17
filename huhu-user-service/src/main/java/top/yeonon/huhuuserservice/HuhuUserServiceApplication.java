package top.yeonon.huhuuserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"top.yeonon.huhuuserservice","top.yeonon.huhucommon"})
public class HuhuUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuhuUserServiceApplication.class, args);
    }

}
