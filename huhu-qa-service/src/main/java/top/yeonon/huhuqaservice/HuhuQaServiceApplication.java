package top.yeonon.huhuqaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients
@ComponentScan(basePackages = {"top.yeonon.huhucommon", "top.yeonon.huhuqaservice"})
public class HuhuQaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuhuQaServiceApplication.class, args);
    }
}
