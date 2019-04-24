package top.yeonon.huhuqaservice.vo.answer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/24 0024 13:29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerApprovalResponseVo {

    private Long id;

    private Long approvalCount;
}
