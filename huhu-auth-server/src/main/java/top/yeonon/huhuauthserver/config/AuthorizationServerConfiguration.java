package top.yeonon.huhuauthserver.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import top.yeonon.huhuauthserver.properties.AuthClientProperties;
import top.yeonon.huhuauthserver.properties.HuhuAuthProperties;

import java.util.List;


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

    private final JwtTokenEnhancer jwtTokenEnhancer;

    @Autowired
    public AuthorizationServerConfiguration(AuthenticationManager authenticationManager,
                                            RedisConnectionFactory redisConnectionFactory,
                                            @Qualifier("domainUserDetailsService") UserDetailsService userDetailsService,
                                            HuhuAuthProperties huhuAuthProperties, JwtTokenEnhancer jwtTokenEnhancer) {
        this.authenticationManager = authenticationManager;
        this.redisConnectionFactory = redisConnectionFactory;
        this.userDetailsService = userDetailsService;
        this.huhuAuthProperties = huhuAuthProperties;
        this.jwtTokenEnhancer = jwtTokenEnhancer;
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(huhuAuthProperties.getJwt().getSignKey());
        return accessTokenConverter;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //这里必须得先setBuilder，否则会出现异常
        clients.setBuilder(clients.inMemory());
        Integer accessTokenExpireTime = huhuAuthProperties.getJwt().getAccessTokenExpireTime();
        Integer refreshTokenExpireTime = huhuAuthProperties.getJwt().getRefreshTokenExpireTime();
        for (AuthClientProperties client : huhuAuthProperties.getClients()) {
            String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode(client.getSecret());
            clients.and()
                    .withClient(client.getClientId())
                    .authorizedGrantTypes(client.getAuthorizedGrantTypes().split(","))
                    .scopes(client.getScopes().split(","))
                    .authorities(client.getAuthorities().split(","))
                    .secret(finalSecret)
                    .accessTokenValiditySeconds(accessTokenExpireTime)
                    .refreshTokenValiditySeconds(refreshTokenExpireTime);
        }
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .userDetailsService(userDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);


        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancerList = Lists.newArrayList();
        enhancerList.add(jwtTokenEnhancer);
        enhancerList.add(jwtAccessTokenConverter());

        enhancerChain.setTokenEnhancers(enhancerList);

        endpoints.tokenEnhancer(enhancerChain)
                .accessTokenConverter(jwtAccessTokenConverter());
    }
}
