package top.yeonon.huhuauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
@ComponentScan(basePackages = {"top.yeonon.huhuauthserver", "top.yeonon.huhucommon"})
public class HuhuAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuhuAuthServerApplication.class, args);
    }

}
