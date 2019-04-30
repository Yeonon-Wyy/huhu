package top.yeonon.huhusearchservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 17:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchQuestionResponseVo {

    private List<QuestionInfo> questionInfoList;

    private Integer pageNum;

    private Integer pageSize;

    private boolean hasNext;

    private boolean hasPrevious;

    private boolean isFirst;

    private boolean isLast;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionInfo {
        private Long id;
        private String title;
        //摘要或者叫做总结
        private String summary;
        private Integer followerCount;
        private Integer answerCount;
        private Integer commentCount;
        private Integer status;
        private Date createTime;
        private Date updateTime;
    }
}
