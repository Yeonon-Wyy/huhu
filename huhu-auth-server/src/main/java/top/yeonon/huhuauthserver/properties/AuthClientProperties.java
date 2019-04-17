package top.yeonon.huhuauthserver.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 16:45
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthClientProperties {

    private String clientId;
    private String authorizedGrantTypes;
    private String scopes;
    private String authorities;
    private String secret;
}
