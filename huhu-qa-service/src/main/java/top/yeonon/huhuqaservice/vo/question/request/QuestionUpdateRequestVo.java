package top.yeonon.huhuqaservice.vo.question.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhuqaservice.entity.Question;

import java.util.Date;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 15:57
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionUpdateRequestVo {

    private Long id;

    private Long userId;

    private String title;

    private String content;

    public boolean validate() {
        return id != null
                && id > 0
                && userId != null
                && userId > 0;
    }

    public Question update(Question question) {
        if (StringUtils.isNotEmpty(title)) {
            question.setTitle(title);
        }
        if (StringUtils.isNotEmpty(content)) {
            question.setContent(content);
        }
        question.setUpdateTime(new Date());
        return question;
    }
}
