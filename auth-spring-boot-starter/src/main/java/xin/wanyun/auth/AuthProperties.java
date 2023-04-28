package xin.wanyun.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 此为Auth模块配置类，需要注意的是，guards与guard参数只能存在一个，guards用于在多端项目中使用（即多种不同表实现用户登录）。
 * 此外，guard与guards都配置后，guard的优先级更高。若不配置guard，走guards的规则。other参数，仅在多端项目中生效。
 * 假设有前台与后台之分，前台为home，后台为admin，此时other配置为true，如果存在其他端路由example，请求时将不会被拦截，反之亦然。
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    /**
     * 守卫
     */
    private Guard guard;

    /**
     * 守卫组
     */
    private List<Guard> guards;

    /**
     * 是否放行除了守卫以外的路由
     */
    private boolean other = true;
}
