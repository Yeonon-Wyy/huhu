package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/15 0015 12:59
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerQueryRequestVo {

    private Long id;

    public boolean validate() {
        return CommonUtils.checkId(id);
    }
}
