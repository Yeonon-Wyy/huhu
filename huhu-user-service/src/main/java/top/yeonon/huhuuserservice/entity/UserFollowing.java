package top.yeonon.huhuuserservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author yeonon
 * @date 2019/4/14 0014 17:03
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user_following")
public class UserFollowing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11, nullable = false)
    private Long id;

    @Column(name = "user_id", length = 11, nullable = false)
    private Long userId;

    @Column(name = "following_id", length = 11, nullable = false)
    private Long followingId;

    public UserFollowing(Long userId, Long followingId) {
        this.userId = userId;
        this.followingId = followingId;
    }
}
