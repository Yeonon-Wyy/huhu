package top.yeonon.huhuqaservice.vo.question.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.request.RequestVo;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 13:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQueryRequestVo implements RequestVo {

    private Long id;

    public boolean validate() {
        return id != null
                && id > 0;
    }
}
