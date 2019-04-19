package top.yeonon.huhuuserservice.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 14:42
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtProperties {

    private String signKey;
}
