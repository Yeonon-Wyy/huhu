package top.yeonon.huhuuserservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.yeonon.huhuuserservice.interceptor.CheckIdInterceptor;

/**
 * @Author yeonon
 * @date 2019/4/18 0018 17:36
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckIdInterceptor());
    }
}
