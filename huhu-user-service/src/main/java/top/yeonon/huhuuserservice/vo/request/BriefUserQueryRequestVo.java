package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.request.RequestVo;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/5/15 0015 12:41
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BriefUserQueryRequestVo implements RequestVo {

    private Long userId;

    public boolean validate() {
        return CommonUtils.checkId(userId);
    }
}
