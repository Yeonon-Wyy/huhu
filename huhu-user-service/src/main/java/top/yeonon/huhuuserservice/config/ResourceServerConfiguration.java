package top.yeonon.huhuuserservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.context.request.RequestContextListener;
import top.yeonon.huhuuserservice.properties.HuhuSecurityProperties;


/**
 * @Author yeonon
 * @date 2019/4/15 0015 17:19
 **/
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    private final HuhuSecurityProperties huhuSecurityProperties;

    @Autowired
    public ResourceServerConfiguration(HuhuSecurityProperties huhuSecurityProperties) {
        this.huhuSecurityProperties = huhuSecurityProperties;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(HttpMethod.DELETE, huhuSecurityProperties.getAuthenticationPath()).authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.POST, huhuSecurityProperties.getAuthenticationPath()).authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.PUT, huhuSecurityProperties.getAuthenticationPath()).authenticated();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/users").authenticated();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //不知道是什么原因，如果不加这个Bean的话，Spring就无法创建
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
}
