package top.yeonon.huhuqaservice.vo.question.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 14:10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionFollowRequestVo {

    private Long questionId;

    private Long userId;

    public boolean validate() {
        return CommonUtils.chekcId(questionId)
                && CommonUtils.chekcId(userId);
    }
}
