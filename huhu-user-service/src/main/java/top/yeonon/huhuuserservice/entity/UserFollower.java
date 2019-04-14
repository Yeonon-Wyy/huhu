package top.yeonon.huhuuserservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 16:59
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_follower")
public class UserFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11, nullable = false)
    private Long id;

    @Column(name = "user_id", length = 11, nullable = false)
    private Long userId;

    @Column(name = "follower_id", length = 11, nullable = false)
    private Long followerId;


    public UserFollower(Long userId, Long followerId) {
        this.userId = userId;
        this.followerId = followerId;
    }
}
