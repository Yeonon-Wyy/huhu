package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 16:32
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestVo {

    private String username;
    private String password;

    public boolean validate() {
        return StringUtils.isNotEmpty(username)
                && StringUtils.isNotEmpty(password);
    }
}
