package top.yeonon.huhuuserservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryResponseVo {

    private String username;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private Integer status;
    private Integer sex;
    private Integer followerCount;
    private Integer followingCount;
    private String profile;
    private String industry;
    private Integer degree;
}
