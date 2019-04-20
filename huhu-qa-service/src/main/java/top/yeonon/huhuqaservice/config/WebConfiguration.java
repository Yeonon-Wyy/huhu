package top.yeonon.huhuqaservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.yeonon.huhuqaservice.interceptor.CheckUserIdInterceptor;
/**
 * @Author yeonon
 * @date 2019/4/20 0020 13:41
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final CheckUserIdInterceptor checkUserIdInterceptor;

    @Autowired
    public WebConfiguration(CheckUserIdInterceptor checkUserIdInterceptor) {
        this.checkUserIdInterceptor = checkUserIdInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkUserIdInterceptor);
    }
}
