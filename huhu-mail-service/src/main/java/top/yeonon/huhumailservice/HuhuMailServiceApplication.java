package top.yeonon.huhumailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"top.yeonon.huhumailservice", "top.yeonon.huhucommon"})
public class HuhuMailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuhuMailServiceApplication.class, args);
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        return threadPoolTaskExecutor;
    }

}
