package top.yeonon.huhuqaservice.vo.answer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 17:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateRequestVo {

    private Long questionId;

    private Long userId;

    private String content;

    public boolean validate() {
        return questionId != null
                && questionId > 0
                && userId != null
                && userId > 0
                && StringUtils.isNotEmpty(content);
    }
}
