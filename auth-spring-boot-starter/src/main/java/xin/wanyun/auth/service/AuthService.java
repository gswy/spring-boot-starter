package xin.wanyun.auth.service;

import xin.wanyun.auth.exception.AuthException;
import xin.wanyun.auth.response.JwtResponse;
import xin.wanyun.auth.BaseUser;

public interface AuthService {

    JwtResponse login(BaseUser user, String guard) throws AuthException;

}
