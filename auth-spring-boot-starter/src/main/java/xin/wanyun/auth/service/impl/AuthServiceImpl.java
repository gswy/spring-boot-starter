package xin.wanyun.auth.service.impl;

import xin.wanyun.auth.AuthProperties;
import xin.wanyun.auth.BaseUser;
import xin.wanyun.auth.Guard;
import xin.wanyun.auth.exception.AuthException;
import xin.wanyun.auth.response.JwtResponse;
import xin.wanyun.auth.service.AuthService;
import xin.wanyun.auth.utils.JwtTokenUtil;

import java.util.Objects;

public class AuthServiceImpl implements AuthService {

    /**
     * 注入配置类
     */
    private final AuthProperties properties;

    public AuthServiceImpl(AuthProperties properties) {
        this.properties = properties;
    }

    @Override
    public JwtResponse login(BaseUser user, String guardName) throws AuthException {
        Guard guard = properties.getGuards().stream().filter(item -> item.getPrefix().equals(guardName)).findFirst().orElse(null);
        if (Objects.isNull(guard)) {
            throw new AuthException("The guard name used for login is not in the configuration file");
        }
        String token = JwtTokenUtil.createToken(user.getIdentity(), guard);
        return new JwtResponse(token, guard.getConfig().getExpired());
    }

    @Override
    public JwtResponse login(BaseUser user) throws AuthException {
        String token = JwtTokenUtil.createToken(user.getIdentity(), properties.getGuard());
        return new JwtResponse(token, properties.getGuard().getConfig().getExpired());
    }

}
