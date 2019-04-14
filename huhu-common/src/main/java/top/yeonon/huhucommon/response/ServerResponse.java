package top.yeonon.huhucommon.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/4/9 0009 19:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponse<T> {

    private Integer code;
    private String message;
    private T data;


}
