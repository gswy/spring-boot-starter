package com.wanyun.auth.service;

import com.wanyun.auth.exception.AuthException;
import com.wanyun.auth.response.JwtResponse;
import com.wanyun.auth.BaseUser;

public interface AuthService {

    public JwtResponse login(BaseUser user, String guard) throws AuthException;

}
