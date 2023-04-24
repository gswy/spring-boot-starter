package xin.wanyun.auth.service.impl;

import xin.wanyun.auth.AuthConfig;
import xin.wanyun.auth.BaseUser;
import xin.wanyun.auth.Guard;
import xin.wanyun.auth.exception.AuthException;
import xin.wanyun.auth.response.JwtResponse;
import xin.wanyun.auth.service.AuthService;
import xin.wanyun.auth.utils.JwtTokenUtil;
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
