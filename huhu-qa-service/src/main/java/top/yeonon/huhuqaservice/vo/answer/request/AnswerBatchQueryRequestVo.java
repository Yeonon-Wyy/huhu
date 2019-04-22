package top.yeonon.huhuqaservice.vo.answer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * 答案不能脱离问题单独存在，而且本身内容算不是多，也不存在私密信息，故直接批量分页获取即可
 * @Author yeonon
 * @date 2019/4/20 0020 18:13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerBatchQueryRequestVo {


    private Long questionId;

    private Integer pageNum;

    private Integer pageSize;


    public boolean validate() {
        return CommonUtils.checkId(questionId);
    }
}
