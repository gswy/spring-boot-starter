package xin.wanyun.auth;

import xin.wanyun.auth.utils.JwtConfig;
import lombok.Data;

import java.util.List;

@Data
public class Guard {

    private JwtConfig config;

    /**
     * 路由前缀（例如：admin，home等）
     */
    private String prefix = null;

    /**
     * 对应的用户类
     */
    private String userClassName;

    /**
     * 路由列表（允许放行）
     */
    private List<Route> routes;

}
