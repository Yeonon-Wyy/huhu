package top.yeonon.huhuuserservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/5/15 0015 12:42
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BriefUserQueryResponseVo {


    private BriefUserInfo briefUserInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BriefUserInfo {
        private String username;
        private Integer status;
        private String avatar;
        private Integer sex;
        private Integer followerCount;
        private Integer followingCount;
        private String profile;
        private Integer degree;
    }
}
