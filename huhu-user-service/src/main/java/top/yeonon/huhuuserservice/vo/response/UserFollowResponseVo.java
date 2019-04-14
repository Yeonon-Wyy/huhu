package top.yeonon.huhuuserservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:02
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserFollowResponseVo {

    //返回发起关注请求的用户ID
    private Long id;
}
