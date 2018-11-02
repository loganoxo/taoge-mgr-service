package com.zhibi.taoge.config.security;

import com.zhibi.taoge.entity.console.User;
import com.zhibi.taoge.service.admin.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by QinHe on 2018-10-21.
 */

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RedisTemplate adminRedisTemplate;
    private final IUserService userService;

    public UserDetailsServiceImpl(RedisTemplate adminRedisTemplate,
                                  IUserService userService) {
        this.adminRedisTemplate = adminRedisTemplate;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (StringUtils.isBlank(userName)) {
            throw new UsernameNotFoundException("用户名为空！");
        }
        User user = userService.findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("没有这个用户！");
        }
        return new SecurityUserDetails(user);
    }
}
