package top.yeonon.huhuauthserver.config;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import top.yeonon.huhuauthserver.entity.User;
import top.yeonon.huhuauthserver.repository.UserRepository;
import top.yeonon.huhucommon.response.ResponseCode;

import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/4/18 0018 19:56
 **/
@Component
@Slf4j
public class JwtTokenEnhancer implements TokenEnhancer {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error(ResponseCode.INVALID_USERNAME.getDescription());
            return accessToken;
        }
        Map<String, Object> info = Maps.newHashMap();
        info.put("id", user.getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
