package top.yeonon.huhuuserservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/15 0015 13:44
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBatchQueryResponseVo {

    private List<UserInfo> userInfoList;

    private Integer pageNum;
    private Integer pageSize;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String avatar;
        private Integer sex;
        private String address;
        private Integer followerCount;
        private Integer followingCount;
        private String profile;
        private String industry;
    }
}
