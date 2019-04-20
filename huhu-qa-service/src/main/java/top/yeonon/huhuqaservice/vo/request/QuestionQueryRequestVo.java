package top.yeonon.huhuqaservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/20 0020 13:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionQueryRequestVo {

    private Long id;

    public boolean validate() {
        return id != null
                && id > 0;
    }
}
