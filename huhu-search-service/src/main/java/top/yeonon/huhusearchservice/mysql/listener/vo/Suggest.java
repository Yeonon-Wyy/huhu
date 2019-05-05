package top.yeonon.huhusearchservice.mysql.listener.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author yeonon
 * @date 2019/5/5 0005 19:48
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggest {

    private String input;

    private Integer weight;
}
