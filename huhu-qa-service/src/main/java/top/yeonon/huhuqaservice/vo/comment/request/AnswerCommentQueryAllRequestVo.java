package top.yeonon.huhuqaservice.vo.comment.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/22 0022 11:37
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCommentQueryAllRequestVo {

    private Long answerId;

    private Integer pageNum;

    private Integer pageSize;

    public boolean validate() {
        return CommonUtils.checkId(answerId);
    }
}
