package top.yeonon.huhuqaservice.vo.question.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhucommon.request.RequestVo;

import java.util.Set;

/**
 * @Author yeonon
 * @date 2019/4/19 0019 19:00
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateRequestVo implements RequestVo {

    private Long userId;

    private String title;

    private String content;

    private Set<String> tagNames;

    //tag可以为空
    @Override
    public boolean validate() {
        return userId != null
                && userId > 0
                && StringUtils.isNotEmpty(title)
                && StringUtils.isNotEmpty(content);
    }
}
