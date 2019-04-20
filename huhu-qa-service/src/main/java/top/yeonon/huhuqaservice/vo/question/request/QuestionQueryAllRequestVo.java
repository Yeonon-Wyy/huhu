package top.yeonon.huhuqaservice.vo.question.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 19:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQueryAllRequestVo {

    private Integer pageNum;

    private Integer pageSize;
}
