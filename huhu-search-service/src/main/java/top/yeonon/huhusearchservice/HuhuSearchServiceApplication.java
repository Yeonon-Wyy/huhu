package top.yeonon.huhusearchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"top.yeonon.huhucommon", "top.yeonon.huhusearchservice"})
public class HuhuSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuhuSearchServiceApplication.class, args);

    }

}
