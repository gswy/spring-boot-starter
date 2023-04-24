package xin.wanyun.auth.utils;

import lombok.Data;

@Data
public class JwtConfig {

    private String secret;

    private Long expired;

    private String guard;

}
