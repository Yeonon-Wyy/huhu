package top.yeonon.huhuqaservice.vo.question.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 *
 * @Author yeonon
 * @date 2019/4/20 0020 19:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQueryAllResponseVo {

    private List<QuestionInfo> questionInfoList;

    private Integer pageNum;

    private Integer pageSize;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionInfo {
        private Long id;
        private String title;
        private Date createTime;
        private Date updateTime;
    }
}
