package top.yeonon.huhuqaservice.vo.answer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 18:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerBatchQueryResponseVo {

    private List<AnswerInfo> answerInfoList;

    private Integer pageNum;
    private Integer pageSize;

    private boolean hasNext;

    private boolean hasPrevious;

    private boolean isFirst;

    private boolean isLast;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerInfo {
        //用于获取关联的评论
        private Long id;
        //用于获取用户信息，标识答案所属
        private Long userId;
        private String content;
        private Integer status;
        private Long approvalCount;
        private Long commentCount;
        private Date createTime;
        private Date updateTime;
    }
}
