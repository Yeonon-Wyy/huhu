package top.yeonon.huhuqaservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 19:43
 **/
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/question/**", "/answers/**", "/comments/**")
                .authenticated();
    }

    //不知道是什么原因，如果不加这个Bean的话，Spring就无法创建
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
