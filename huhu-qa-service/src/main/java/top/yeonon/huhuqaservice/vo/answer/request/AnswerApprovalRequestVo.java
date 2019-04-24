package top.yeonon.huhuqaservice.vo.answer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/23 0023 12:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerApprovalRequestVo {

    private Long answerId;

    private Long userId;

    public boolean validate() {
        return CommonUtils.checkId(answerId, userId);
    }
}
