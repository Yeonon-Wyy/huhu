package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:00
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserFollowRequestVo {

    //发起关注请求的用户ID
    private Long userId;

    //被关注的用户ID
    private Long followUserId;

    public boolean validate() {
        return userId != null
                && userId > 0
                && followUserId != null
                && followUserId > 0;
    }
}
