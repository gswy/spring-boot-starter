# auth-spring-boot-starter

## 配置application.yaml
```yaml
# 配置Auth模块
auth:
  guards:
    # 配置admin守卫
    - prefix: admin
      # 用户实体类
      user-class-name: com.example.demo.entity.AdminUser
      # Jwt配置，过期毫秒和jwtKey
      config:
        expired: 3500000
        secret: R50LU7RlfsIINMzS0qDFkJnNoFRr7iRh
      # 需要放行的路由
      routes:
        # 单个路由，请求方式，请求路径
        - method: GET
          path: /login
        - method: GET
          path: /captcha

    # 配置home守卫
    - prefix: home
      # 用户实体类
      user-class-name: com.example.demo.entity.HomeUser
      # Jwt配置，过期毫秒和jwtKey
      config:
        expired: 3600000
        secret: XHDfb5ifFoj6TTMRq4qRfB3o1qm1zepQ
      # 需要放行的路由
      routes:
        # 单个路由，请求方式，请求路径
        - method: GET
          path: /login
        - method: GET
          path: /captcha
```

## 入口文件
```java
package com.renjie.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 必要扫描包 xin.wanyun.auth，当前包
@SpringBootApplication(scanBasePackages = {"xin.wanyun.auth", "com.example.application"})
@MapperScan("com.renjie.server.mapper")
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
```

## 用户实体类
```java
package com.example.demo.entity;

@Data
// 指定对应表
@TableName("admin_users")
public class AdminUser implements BaseUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private Boolean status;

    private Date createdAt;

    private Date updatedAt;

    // 返回用户权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    // 返回账户是否过期，如业务没有过期，则返回false
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    // 返回账户是否锁定
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    // 返回账户令牌是否过期
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 返回账户是否开启状态
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 返回数据表中能确定唯一性的列数据，一般返回用户主键。
    @Override
    public String getIdentity() {
        return this.id.toString();
    }
}
```

## 控制器中使用
```java
package com.example.demo.controller;

@RestController
@RequestMapping("/admin")
public class AdminController {
    
    // 注入登录服务类
    @Autowired
    AuthService authService;

    @GetMapping("/index")
    public ResponseEntity<Object> index() {
        return ResponseEntity.ok("后台首页");
    }

    @GetMapping("/login")
    public ResponseEntity<Object> login() throws AuthException {
        AdminUser adminUser = new AdminUser();
        adminUser.setId(1L);
        // 模拟用户，并登录返回Jwt的token以及过期毫秒数。
        return ResponseEntity.ok(authService.login(adminUser, "admin"));
    }

}

```
