package top.yeonon.huhuauthserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import top.yeonon.huhuauthserver.constant.Const;
import top.yeonon.huhuauthserver.constant.UserRole;
import top.yeonon.huhuauthserver.entity.User;
import top.yeonon.huhuauthserver.repository.UserRepository;

/**
 * @Author yeonon
 * @date 2019/4/16 0016 16:02
 **/
@Component
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            String role = Const.UserConst.ROLE_PREFIX + UserRole.valueOf(user.getRole());
            String password = user.getPassword();
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    password,
                    AuthorityUtils.createAuthorityList(role)
            );
        } else {
            throw new UsernameNotFoundException("用户["+username+"]不存在");
        }
    }
}
