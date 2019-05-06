package top.yeonon.huhusearchservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/5/5 0005 18:21
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutoCompletionResponseVo {

    private List<Suggestion> suggestionList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Suggestion {
        private String text;
        private Float score;
    }
}
