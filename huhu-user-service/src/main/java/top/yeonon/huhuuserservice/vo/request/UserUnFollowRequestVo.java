package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 18:00
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUnFollowRequestVo {

    private Long userId;

    private Long unFollowId;

    public boolean validate() {
        return userId != null
                && userId > 0
                && unFollowId != null
                && unFollowId > 0;
    }
}
