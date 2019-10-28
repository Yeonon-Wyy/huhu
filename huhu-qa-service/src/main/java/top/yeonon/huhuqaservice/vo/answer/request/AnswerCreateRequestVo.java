package top.yeonon.huhuqaservice.vo.answer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhucommon.request.RequestVo;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 17:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateRequestVo implements RequestVo {

    private Long questionId;

    private Long userId;

    private String content;

    public boolean validate() {
        return CommonUtils.checkId(questionId, userId)
                && StringUtils.isNotEmpty(content);
    }
}
