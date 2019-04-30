package top.yeonon.huhusearchservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 17:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchQuestionRequestVo {

    private Integer pageNum;

    private Integer pageSize;

    private String keyword;

    public boolean validate() {
        return StringUtils.isNotEmpty(keyword);
    }
}
