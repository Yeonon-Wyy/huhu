package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestVo {

    private String username;
    private String password;
    private String email;


    public boolean validate() {
        return StringUtils.isNotEmpty(username)
                && StringUtils.isNotEmpty(password)
                && StringUtils.isNotEmpty(email);
    }

}
