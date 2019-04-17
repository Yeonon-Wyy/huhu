package top.yeonon.huhuuserservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
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
        http.authorizeRequests()
                .antMatchers(huhuSecurityProperties.getAuthenticationPath())
                .authenticated();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
