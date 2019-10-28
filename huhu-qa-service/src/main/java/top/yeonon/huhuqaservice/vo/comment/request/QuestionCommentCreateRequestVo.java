package top.yeonon.huhuqaservice.vo.comment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhucommon.request.RequestVo;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 19:11
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCommentCreateRequestVo implements RequestVo {

    private Long userId;

    private Long questionId;

    private String content;

    public boolean validate() {
        return CommonUtils.checkId(userId, questionId)
                && StringUtils.isNotEmpty(content);
    }
}
