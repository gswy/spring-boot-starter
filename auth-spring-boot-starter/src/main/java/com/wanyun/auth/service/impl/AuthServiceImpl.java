package com.wanyun.auth.service.impl;

import com.wanyun.auth.AuthConfig;
import com.wanyun.auth.BaseUser;
import com.wanyun.auth.Guard;
import com.wanyun.auth.exception.AuthException;
import com.wanyun.auth.response.JwtResponse;
import com.wanyun.auth.service.AuthService;
import com.wanyun.auth.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    /**
     * 注入配置类
     */
    @Autowired
    AuthConfig config;

    @Override
    public JwtResponse login(BaseUser user, String guardName) throws AuthException {
        Guard guard = config.getGuards().stream().filter(item -> item.getPrefix().equals(guardName)).findFirst().orElse(null);
        if (Objects.isNull(guard)) {
            throw new AuthException("The guard name used for login is not in the configuration file");
        }
        String token = JwtTokenUtil.createToken(user.getIdentity(), guard);
        return new JwtResponse(token, guard.getConfig().getExpired());
    }


}
