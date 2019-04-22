package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import top.yeonon.huhucommon.utils.CommonUtils;
import top.yeonon.huhuuserservice.entity.User;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:31
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestVo {

    private Long id;
    private String address;
    private String avatar;
    private Integer sex;
    private String profile;
    private String industry;
    private Integer degree;

    public boolean validate() {
        return CommonUtils.checkId(id);
    }

    public User updateUser(User user) {
        if (StringUtils.isNotEmpty(address)) {
            user.setAddress(address);
        }
        if (StringUtils.isNotEmpty(avatar)) {
            user.setAvatar(avatar);
        }
        if (sex != null) {
            user.setSex(sex);
        }
        if (StringUtils.isNotEmpty(profile)) {
            user.setProfile(profile);
        }
        if (StringUtils.isNotEmpty(industry)) {
            user.setIndustry(industry);
        }
        if (degree != null) {
            user.setDegree(degree);
        }
        return user;
    }
}
