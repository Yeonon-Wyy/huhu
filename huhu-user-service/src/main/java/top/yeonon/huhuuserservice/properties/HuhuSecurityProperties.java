package top.yeonon.huhuuserservice.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 18:47
 **/
@Configuration
@ConfigurationProperties(prefix = "huhu.security")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuhuSecurityProperties {

    private String[] authenticationPath;
}
