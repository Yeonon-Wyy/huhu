package top.yeonon.huhuqaservice.vo.answer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 19:19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDeleteRequestVo {

    private Long id;

    private Long userId;

    public boolean validate() {
        return id != null
                && id > 0;
    }

}
