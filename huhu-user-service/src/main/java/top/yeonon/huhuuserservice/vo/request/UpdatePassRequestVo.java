package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Author yeonon
 * @date 2019/4/18 0018 17:19
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePassRequestVo {

    private String username;

    private String validateCode;

    private String newPassword;

    public boolean validate() {
        return StringUtils.isNotEmpty(newPassword)
                && StringUtils.isNotEmpty(validateCode)
                && StringUtils.isNotEmpty(username);
    }
}
