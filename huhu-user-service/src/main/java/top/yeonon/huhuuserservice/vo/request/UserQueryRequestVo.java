package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryRequestVo {

    private Long id;

    public boolean validate() {
        return CommonUtils.chekcId(id);
    }
}
