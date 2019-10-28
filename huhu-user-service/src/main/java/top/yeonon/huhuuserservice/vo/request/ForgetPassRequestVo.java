package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhucommon.request.RequestVo;

/**
 * @Author yeonon
 * @date 2019/4/18 0018 17:01
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgetPassRequestVo implements RequestVo {

    //username 和 email是用户填写的
    private String username;

    private String email;

    public boolean validate() {
        return StringUtils.isNotEmpty(username)
                && StringUtils.isNotEmpty(email);
    }

}
