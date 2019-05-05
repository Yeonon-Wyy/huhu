package top.yeonon.huhusearchservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Author yeonon
 * @date 2019/5/5 0005 18:20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoCompletionRequestVo {

    private String text;

    public boolean validate() {
        return StringUtils.isNotEmpty(text);
    }

}
