package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:38
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDeleteRequestVo {

    private Long id;

    public boolean validate() {
        return id != null
                && id > 0;
    }
}
