package top.yeonon.huhuqaservice.vo.comment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 19:35
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCommentCreateRequestVo {

    private Long userId;

    private Long answerId;

    private String content;


    public boolean validate() {
        return CommonUtils.chekcId(userId)
                && CommonUtils.chekcId(answerId)
                && StringUtils.isNotEmpty(content);

    }
}
