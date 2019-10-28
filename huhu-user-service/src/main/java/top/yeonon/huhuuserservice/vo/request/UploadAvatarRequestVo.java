package top.yeonon.huhuuserservice.vo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import top.yeonon.huhucommon.request.RequestVo;
import top.yeonon.huhucommon.utils.CommonUtils;

/**
 * @Author yeonon
 * @date 2019/4/22 0022 18:00
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadAvatarRequestVo implements RequestVo {

    private Long userId;

    private MultipartFile file;

    public boolean validate() {
        return CommonUtils.checkId(userId);
    }
}
