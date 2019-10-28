package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import top.yeonon.huhucommon.request.RequestVo;

import java.util.List;

/**
 * @Author yeonon
 * @date 2019/4/15 0015 13:43
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBatchQueryRequestVo implements RequestVo {

    private List<Long> ids;

    private Integer pageNum;
    private Integer pageSize;

    public boolean validate() {
        return CollectionUtils.isNotEmpty(ids)
                && ids.stream().allMatch(id -> id > 0)
                && pageNum >= 0
                && pageSize >= 1;
    }
}
