package top.yeonon.huhuauthserver.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 16:34
 **/
@ConfigurationProperties(prefix = "huhu.auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class HuhuAuthProperties {


    private List<AuthClientProperties> clients = new ArrayList<>();

    private JwtProperties jwt = new JwtProperties();

}
