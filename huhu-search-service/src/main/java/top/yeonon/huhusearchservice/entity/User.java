package top.yeonon.huhusearchservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @Author yeonon
 * @date 2019/4/30 0030 19:16
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(indexName = "huhu-qa", type = "user")
public class User {

    @Id
    private String id;

    private String username;

    private String email;

    private String phone;

    private String address;

    private Integer status;

    private String avatar;

    private Integer sex;

    private Integer role;

    private Integer followerCount;

    private Integer followingCount;

    private String profile;

    private String industry;

    private Integer degree;
}
