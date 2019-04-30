package top.yeonon.huhusearchservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 18:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchAnswerResponseVo {

    private List<AnswerInfo> answerInfoList;

    private Integer pageNum;

    private Integer pageSize;

    private boolean hasNext;

    private boolean hasPrevious;

    private boolean isFirst;

    private boolean isLast;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnswerInfo {
        private Long id;
        private Long userId;
        private String summary;
        private Integer status;
        private Long approvalCount;
        private Long commentCount;
        private Date createTime;
        private Date updateTime;
    }
}
