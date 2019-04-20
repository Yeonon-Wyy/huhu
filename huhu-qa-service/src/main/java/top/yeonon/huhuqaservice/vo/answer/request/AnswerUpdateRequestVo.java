package top.yeonon.huhuqaservice.vo.answer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhuqaservice.entity.Answer;

import java.util.Date;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 19:12
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerUpdateRequestVo {

    private Long userId;

    private Long id;

    private String content;

    public boolean validate() {
        return id != null
                && id > 0
                && userId != null
                && userId > 0;
    }

    public Answer update(Answer answer) {
        if (StringUtils.isNotEmpty(this.content)) {
            answer.setContent(content);
        }
        answer.setUpdateTime(new Date());
        return answer;
    }
}
