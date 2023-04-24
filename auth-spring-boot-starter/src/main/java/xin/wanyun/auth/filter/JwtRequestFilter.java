package xin.wanyun.auth.filter;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import xin.wanyun.auth.Guard;
import xin.wanyun.auth.mapper.BaseUserMapper;
import xin.wanyun.auth.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * 守卫
     */
    private final Guard guard;

    /**
     * 数据库查询类
     */
    private final BaseUserMapper baseUserMapper;

    public JwtRequestFilter(Guard guard, BaseUserMapper baseUserMapper) {
        this.guard = guard;
        this.baseUserMapper = baseUserMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("Authorization");
        if (Objects.isNull(token)) {
            token = request.getParameter("token");
        }

        if (Objects.isNull(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 检测token
        String identity = JwtTokenUtil.verifyToken(token, guard);
        if (Objects.nonNull(identity)) {
            // 查询用户（此处需要设法取到不同守卫对应的实体类对象）
            try {
                Class<?> aClass = Class.forName(this.guard.getUserClassName());
                TableInfo tableInfo = TableInfoHelper.getTableInfo(aClass);
                Map<String, Object> user = baseUserMapper.findUserById(tableInfo, identity);
                // 设置上下文用户
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (ClassNotFoundException e) {
                log.info(e.getMessage());
                throw new ServletException("Entity class not found: " + this.guard.getUserClassName());
            }
        }

        filterChain.doFilter(request, response);
    }



}
