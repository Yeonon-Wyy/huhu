package top.yeonon.huhuuserservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author yeonon
 * @date 2019/4/16 0016 17:59
 **/
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    //排除某些URL
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.POST, "/users")
                .antMatchers("/users/password/**");
    }
}
