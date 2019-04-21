package top.yeonon.huhuqaservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 13:33
 **/
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    //排除某些URL
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.GET, "/question/**")
                .antMatchers(HttpMethod.GET, "/answers/**")
                .antMatchers(HttpMethod.GET, "/comments/**");
    }
}
