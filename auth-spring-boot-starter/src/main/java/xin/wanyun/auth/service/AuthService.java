package xin.wanyun.auth.service;

import xin.wanyun.auth.exception.AuthException;
import xin.wanyun.auth.response.JwtResponse;
import xin.wanyun.auth.BaseUser;

public interface AuthService {

    /**
     * 根据不同端登录
     */
    JwtResponse login(BaseUser user, String guard) throws AuthException;

    /**
     * 登录
     */
    JwtResponse login(BaseUser user) throws AuthException;

}
