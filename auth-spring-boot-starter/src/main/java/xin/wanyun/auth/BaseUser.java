package xin.wanyun.auth;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 实现此类，用于生成用户对应JWT
 */
public interface BaseUser extends UserDetails {

    String getIdentity();

}
