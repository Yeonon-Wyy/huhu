package top.yeonon.huhuuserservice.client.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/4/18 0018 17:07
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteForgetPassRequestVo {

    private String to;

    private Map<String, Object> content;

    public boolean validate() {
        return StringUtils.isNotEmpty(to);
    }
}
