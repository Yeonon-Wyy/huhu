package top.yeonon.huhuuserservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.yeonon.huhuuserservice.interceptor.CheckIdInterceptor;

/**
 * @Author yeonon
 * @date 2019/4/18 0018 17:36
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final CheckIdInterceptor checkIdInterceptor;

    @Autowired
    public WebConfiguration(CheckIdInterceptor checkIdInterceptor) {
        this.checkIdInterceptor = checkIdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkIdInterceptor);
    }
}
