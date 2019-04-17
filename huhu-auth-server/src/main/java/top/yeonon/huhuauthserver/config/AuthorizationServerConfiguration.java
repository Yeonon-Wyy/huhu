package top.yeonon.huhuauthserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import top.yeonon.huhuauthserver.properties.AuthClientProperties;
import top.yeonon.huhuauthserver.properties.HuhuAuthProperties;


/**
 * @Author yeonon
 * @date 2019/4/15 0015 16:20
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {


    private final AuthenticationManager authenticationManager;

    private final RedisConnectionFactory redisConnectionFactory;

    private final UserDetailsService userDetailsService;

    private final HuhuAuthProperties huhuAuthProperties;

    @Autowired
    public AuthorizationServerConfiguration(AuthenticationManager authenticationManager,
                                            RedisConnectionFactory redisConnectionFactory,
                                            @Qualifier("domainUserDetailsService") UserDetailsService userDetailsService,
                                            HuhuAuthProperties huhuAuthProperties) {
        this.authenticationManager = authenticationManager;
        this.redisConnectionFactory = redisConnectionFactory;
        this.userDetailsService = userDetailsService;
        this.huhuAuthProperties = huhuAuthProperties;
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //这里必须得先setBuilder，否则会出现异常
        clients.setBuilder(clients.inMemory());
        for (AuthClientProperties client : huhuAuthProperties.getClients()) {
            String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode(client.getSecret());
            clients.and()
                    .withClient(client.getClientId())
                    .authorizedGrantTypes(client.getAuthorizedGrantTypes().split(","))
                    .scopes(client.getScopes().split(","))
                    .authorities(client.getAuthorities().split(","))
                    .secret(finalSecret);
        }




    }



    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }
}
