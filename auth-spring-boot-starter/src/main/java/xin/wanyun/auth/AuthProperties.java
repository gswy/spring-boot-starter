package xin.wanyun.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    /**
     * 守卫组
     */
    private List<Guard> guards;

    /**
     * 是否放行除了守卫以外的路由
     */
    private boolean other = true;


}
