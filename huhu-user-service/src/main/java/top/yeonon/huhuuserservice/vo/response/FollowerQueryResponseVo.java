package top.yeonon.huhuuserservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/15 0015 13:00
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerQueryResponseVo {

    private List<Long> ids;
}
