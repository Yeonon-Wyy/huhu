package top.yeonon.huhuauthserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 15:00
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11, nullable = false)
    private Long id;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 128)
    private String address;

    @Column(name = "status", length = 4, nullable = false)
    private Integer status;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "sex", length = 2, nullable = false)
    private Integer sex;

    @Column(name = "role", length = 4, nullable = false)
    private Integer role;

    @Column(name = "follower_count", length = 20, nullable = false)
    private Integer followerCount;

    @Column(name = "following_count", length = 20, nullable = false)
    private Integer followingCount;

    @Column(name = "profile", length = 255)
    private String profile;

    @Column(name = "industry", length = 255)
    private String industry;

    @Column(name = "degree", length = 4)
    private Integer degree;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    private Date updateTime;


}
