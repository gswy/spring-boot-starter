package xin.wanyun.auth;

import xin.wanyun.auth.filter.JwtRequestFilter;
import xin.wanyun.auth.mapper.BaseUserMapper;
import xin.wanyun.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@MapperScan("com.wanyun.auth.mapper")
@ConditionalOnClass(AuthService.class)
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

    /**
     * 守卫组
     */
    private List<Guard> guards;

    /**
     * 是否放行除了守卫以外的路由
     */
    private boolean other = true;

    @Autowired
    private BaseUserMapper baseUserMapper;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // 关闭csrf
        httpSecurity
                .csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 放行路由
        for (Guard guard : guards) {
            for (Route route : guard.getRoutes()) {
                httpSecurity
                        .authorizeHttpRequests()
                        .requestMatchers(route.getMethod(), "/" + guard.getPrefix() + route.getPath())
                        .permitAll();
            }
        }

        // 拦截路由
        for (Guard guard : guards) {
            if (Objects.isNull(guard.getConfig().getExpired())) {
                guard.getConfig().setExpired(3600000L);
            }
            if (Objects.isNull(guard.getConfig().getSecret())) {
                guard.getConfig().setSecret("XHDfb5ifFoj6TTMRq4qRfB3o1qm1zepQ");
            }
            guard.getConfig().setGuard(guard.getPrefix());

            httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/" + guard.getPrefix() + "/**")
                .authenticated()
                .and()
                // 身份验证
                .authenticationManager(authenticationManager(authenticationConfiguration))
                // 添加jwt过滤器
                .addFilterBefore(new JwtRequestFilter(guard, baseUserMapper), UsernamePasswordAuthenticationFilter.class);
        }

        // 错误处理器
        httpSecurity
                .exceptionHandling()
                // 未认证处理
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    PrintWriter writer = response.getWriter();
                    writer.write("{\"message\": \"" + authException.getMessage() + "\"}");
                    writer.flush();
                    writer.close();
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    PrintWriter writer = response.getWriter();
                    writer.write("{\"message\": \"" + accessDeniedException.getMessage() + "\"}");
                    writer.flush();
                    writer.close();
                });

        // 是否放行除了守卫以外的路由
        if (this.other) {
            httpSecurity
                    .authorizeHttpRequests()
                    .anyRequest().permitAll();
        }

        return httpSecurity.build();
    }

    public List<Guard> getGuards() {
        return guards;
    }

    public void setGuards(List<Guard> guards) {
        this.guards = guards;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }
}
