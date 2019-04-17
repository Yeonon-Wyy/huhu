package top.yeonon.huhumailservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @Author yeonon
 * @date 2019/4/17 0017 20:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateMessageRequestVo {

    private String from;

    private String to;

    private String subject;

    private Map<String, Object> content;

    public boolean validate() {
        return StringUtils.isNotEmpty(to)
                && StringUtils.isNotEmpty(from)
                && StringUtils.isNotEmpty(subject);
    }
}
