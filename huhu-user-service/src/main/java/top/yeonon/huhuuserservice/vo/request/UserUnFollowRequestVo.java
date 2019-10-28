package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.request.RequestVo;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 18:00
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUnFollowRequestVo implements RequestVo {

    private Long userId;

    private Long unFollowId;

    public boolean validate() {
        return CommonUtils.checkId(userId, unFollowId);
    }
}
