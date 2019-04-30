package top.yeonon.huhusearchservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 19:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserResponseVo {

    List<UserInfo> userInfoList;

    private Integer pageNum;

    private Integer pageSize;

    private boolean hasNext;

    private boolean hasPrevious;

    private boolean isFirst;

    private boolean isLast;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String profile;
        private Integer status;
        private String avatar;
        private Integer sex;
        private Integer followerCount;
        private Integer followingCount;
        private String industry;
        private Integer degree;
    }
}
