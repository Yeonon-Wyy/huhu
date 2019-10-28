package top.yeonon.huhuqaservice.vo.answer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.request.RequestVo;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/21 0021 13:13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerBatchQueryByUserIdRequestVo implements RequestVo {

    private Long userId;

    private Integer pageNum;

    private Integer pageSize;

    public boolean validate() {
        return CommonUtils.checkId(userId);
    }
}
