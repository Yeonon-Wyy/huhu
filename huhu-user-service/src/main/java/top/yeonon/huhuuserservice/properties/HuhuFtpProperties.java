package top.yeonon.huhuuserservice.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author yeonon
 * @date 2019/4/22 0022 17:54
 **/
@Configuration
@ConfigurationProperties(prefix = "huhu.ftp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuhuFtpProperties {

    private String host;
    private Integer port = 21;
    private String username;
    private String password;

    private String serverAddr;

}
