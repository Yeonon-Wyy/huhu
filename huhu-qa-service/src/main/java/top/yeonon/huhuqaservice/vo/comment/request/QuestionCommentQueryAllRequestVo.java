package top.yeonon.huhuqaservice.vo.comment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/22 0022 12:03
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCommentQueryAllRequestVo {

    private Long questionId;

    private Integer pageNum;

    private Integer pageSize;

    public boolean validate() {
        return CommonUtils.checkId(questionId);
    }
}
