package xin.wanyun.auth;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Configuration;
import xin.wanyun.auth.filter.JwtRequestFilter;
import xin.wanyun.auth.mapper.AuthMapper;
import xin.wanyun.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xin.wanyun.auth.service.impl.AuthServiceImpl;

import java.io.PrintWriter;
import java.util.Objects;

@Slf4j
@MapperScan
@Configuration
@EnableAutoConfiguration
@ConditionalOnMissingBean({
        AuthService.class,
        BCryptPasswordEncoder.class,
        AuthenticationManager.class,
        SecurityFilterChain.class,
})
public class AuthConfiguration {

    @Autowired
    AuthProperties properties;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    AuthConfiguration() {
        System.out.println("注入啦");
    }

    @Bean
    public AuthService authService() {
        return new AuthServiceImpl(properties);
    }

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
        for (Guard guard : properties.getGuards()) {
            for (Route route : guard.getRoutes()) {
                httpSecurity
                        .authorizeHttpRequests()
                        .requestMatchers(route.getMethod(), "/" + guard.getPrefix() + route.getPath())
                        .permitAll();
            }
        }

        // 拦截路由
        for (Guard guard : properties.getGuards()) {
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
                    .addFilterBefore(new JwtRequestFilter(guard, authMapper), UsernamePasswordAuthenticationFilter.class);
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
        if (properties.isOther()) {
            httpSecurity
                    .authorizeHttpRequests()
                    .anyRequest().permitAll();
        }

        return httpSecurity.build();
    }
}
