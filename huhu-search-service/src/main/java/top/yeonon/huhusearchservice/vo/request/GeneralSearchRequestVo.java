package top.yeonon.huhusearchservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhucommon.request.RequestVo;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 18:33
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralSearchRequestVo implements RequestVo {

    private Integer pageNum;

    private Integer pageSize;

    private String keyword;

    public boolean validate() {
        return StringUtils.isNotEmpty(keyword);
    }
}
