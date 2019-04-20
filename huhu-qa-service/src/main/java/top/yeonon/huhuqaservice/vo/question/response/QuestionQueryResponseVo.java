package top.yeonon.huhuqaservice.vo.question.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 13:27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQueryResponseVo {

    private String title;

    private String content;

    private Integer followerCount;

    private Integer answerCount;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
