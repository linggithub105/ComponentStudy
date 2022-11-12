# Spring Security集成JWT实现权限认证

## 框架介绍

### Spring Security

我们先来看看[Spring Security官网](https://link.juejin.cn?target=https%3A%2F%2Fspring.io%2Fprojects%2Fspring-security)对其的介绍：

> Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications.
>
> Spring Security 是一个功能强大且高度可定制的身份验证和访问控制框架。 它是保护基于 Spring 的应用程序的事实标准。
>
> Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. Like all Spring projects, the real power of Spring Security is found in how easily it can be extended to meet custom requirements
>
> Spring Security 是一个专注于为 Java 应用程序提供身份验证和授权的框架。 像所有 Spring 项目一样，Spring Security 的真正强大之处在于它可以**轻松扩展以满足自定义需求**

通俗点来讲，`Spring Security` 就是一个权限安全框架，可以为我们的项目提供安全的访问控制。又因为它是`Spring` 家族的一员，所以对集成`Spring` 有着天然的优势。它提供了一组可以在`Spring` 应用上下文中配置的`Bean` ，充分利用了`Spring IoC` ，`DI` 和`AOP` 功能，为我们的项目提供安全的声明式的安全访问控制，减少我们的代码量。

> 但是Spring Security是一个重量级的安全框架，并且有一定的上手难度。Spring Security依赖于Spring 的IOC等功能，对于非Spring项目Spring Security的支持显然也没有Shrio那样好。所以，在技术选型的时候大家一定要根据项目实际出发，切记千万不能一昧的追求新的技术，从而给项目带来不必要的时间成本

### JWT(Json Web Tokens)

我们再来看看[JWT官网的介绍](https://link.juejin.cn?target=https%3A%2F%2Fjwt.io%2Fintroduction):

> JSON Web Token (JWT) is an open standard ([RFC 7519](https://link.juejin.cn?target=https%3A%2F%2Ftools.ietf.org%2Fhtml%2Frfc7519)) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object. This information can be verified and trusted because it is digitally signed. JWTs can be signed using a secret (with the **HMAC** algorithm) or a public/private key pair using **RSA** or **ECDS**
>
> JSON Web Token (JWT) 是一个开放标准 (RFC 7519)，它定义了一种紧凑且自包含的方式，用于在各方之间安全地传输信息作为 JSON 对象。 此信息可以验证和信任，因为它是数字签名的。 JWT 可以使用密钥（使用 HMAC 算法）或使用 RSA 或 ECDSA 的公钥/私钥对进行签名。

JWT的组成：JWT的token格式：header.payload.signature

1. header中用于存放签名的生成算法
2. payload中用于存放用户名
3. token的生成时间和过期时间

JWT的认证流程：

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/20058acbd58f403fb9c33d93c459d9c0~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

可能会有小伙伴有疑问：为什么有了session我们还需要使用token呢？各自有什么优缺点？

这里我就简单的说一下吧，我们先来看看传统的session运行机制：

> 当用户第一次通过浏览器使用用户名和密码访问服务器时，服务器会验证用户数据，验证成功后在服务器端写入session数据，向客户端浏览器返回sessionid，浏览器将sessionid保存在cookie中，当用户再次访问服务器时，会携带sessionid，服务器会拿着sessionid从数据库获取session数据，然后进行用户信息查询，查询到，就会将查询到的用户信息返回，从而实现状态保持。

1. 因为session是保存在服务端的，所以当用户量增大时，服务器的压力会增大
2. CSRF跨站伪造请求攻击，session是基于cookie进行用户识别的, cookie如果被截获，用户就会很容易受到跨站请求伪造的攻击
3. 扩展性不强，即在分布式的情况下因为各个微服务不在同一台服务器上，如何共享session这会是一个需要解决的难题，这里不再展开

而使用token就可以让我们避免上面的两个问题：

1. token是保存在客户端的，这样就减轻了服务端的压力
2. token不是保存在cookie中，通常是放在header里面，并且在签发时服务器会进行加密和签名，在随后的访问中服务器会对签发的token进行校验，而在用户登录时我们可以禁用掉session从而避免CSRF攻击

## RBAC模型介绍

基于角色的访问控制（RBAC）是实施面向企业安全策略的一种有效的访问控制方式。

其基本思想是，对系统操作的各种权限不是直接授予具体的用户，而是在用户集合与权限集合之间建立一个角色集合。每一种角色对应一组相应的权限。一旦用户被分配了适当的角色后，该用户就拥有此角色的所有操作权限。这样做的好处是，不必在每次创建用户时都进行分配权限的操作，只要分配用户相应的角色即可，而且角色的权限变更比用户的权限变更要少得多，这样将简化用户的权限管理，减少系统的开销。

在RBAC模型里面，有3个基础组成部分，分别是：用户、角色和权限，它们之间的关系如下图所示：

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/087fbc68c4c049969c12c1e8dedb1289~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

- User（用户）：每个用户都有唯一的UID识别，并被授予不同的角色
- Role（角色）：不同角色具有不同的权限
- Permission（权限）：访问权限(可以访问的资源列表)
- 用户-角色映射：用户和角色之间的映射关系
- 角色-权限映射：角色和权限之间的映射

RBAC还可以细分为好几种，这里不再展开，感兴趣的小伙伴可以自行查阅

## 准备数据库

我们集成Spring Security需要实现最基本的RBAC模型，所以我们先创建一个名为`security` 的数据库，然后创建如下的表：

用户表：

```sql
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `phone` char(11) NOT NULL COMMENT '电话',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

```

角色表：

```sql
DROP TABLE IF EXISTS `t_roles`;
CREATE TABLE `t_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '角色名',
  `value` varchar(255) NOT NULL COMMENT '角色的英文名',
  `user_count` int(11) NOT NULL COMMENT '角色数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

资源表（权限表）：

```sql
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value` varchar(255) NOT NULL COMMENT '权限字段',
  `uri` varchar(255) NOT NULL COMMENT '资源路径',
  `description` varchar(255) NOT NULL COMMENT '资源描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

用户角色关系表：

```sql
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

角色权限关系表：

```sql
DROP TABLE IF EXISTS `t_role_resource`;
CREATE TABLE `t_role_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

```

## 整合Spring Security

### 引入依赖

```xml
<!--Spring Security-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
<!--JWT-->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.0</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
    <version>0.11.0</version>
    <scope>runtime</scope>
</dependency>
<!--解决生成JWT时base64加密错误-->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>31.0.1-jre</version>
</dependency>
<!--mybatis-plus-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.1</version>
</dependency>
<!--mybatis-plus代码生成器-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.5.1</version>
</dependency>
<!--mybatis-plus代码生成器引擎-->
<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity-engine-core</artifactId>
    <version>2.3</version>
</dependency>
<!--lombok简化代码-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
    <version>1.18.20</version>
</dependency>


```

### 添加生成JWT的工具类

```typescript
package cuit.epoch.pymjl.security.common.utils;

import com.google.common.io.BaseEncoding;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pymjl
 * @date 2022/1/19 21:49
 */
@Component
public class JwtUtils {
    private static long expiration;

    private static String jwtId;

    private static String jwtSecret;
    private static final int TIME_UNIT = 1000;

    /**
     * 创建JWT
     */
    public static String createJWT(Map<String, Object> claims, Long time) {
        //指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date(System.currentTimeMillis());

        SecretKey secretKey = generalKey();
        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        //下面就是在为payload添加各种标准声明和私有声明了
        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                //设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(jwtId)
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey);
        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


    /**
     * 验证jwt
     */
    public static Claims verifyJwt(String token) {
        //签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generalKey();
        Claims claims;
        try {
            //得到DefaultJwtParser
            claims = Jwts.parser()
                    //设置签名的秘钥
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }//设置需要解析的jwt
        return claims;
    }

    /**
     * 由字符串生成加密key
     *
     * @return SecretKey
     */
    public static SecretKey generalKey() {
        String stringKey = jwtSecret;
        byte[] encodedKey = BaseEncoding.base64().decode(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA256");
        return key;
    }

    /**
     * 根据userId和userName生成token
     */
    public static String generateToken(Long userId, String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("nickName", username);
        return createJWT(map, expiration * TIME_UNIT);
    }

    /**
     * 根据userName生成token
     */
    public static String generateToken(String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        return createJWT(map, expiration * TIME_UNIT);
    }

    @Value("${jwt.expiration}")
    public void setTokenExpiredTime(long tokenExpiredTime) {
        JwtUtils.expiration = tokenExpiredTime;
    }

    @Value("${jwt.id}")
    public void setJwtId(String jwtId) {
        JwtUtils.jwtId = jwtId;
    }

    @Value("${jwt.secret}")
    public void setJwtSecret(String jwtSecret) {
        JwtUtils.jwtSecret = jwtSecret;
    }

    public static long getExpiration() {
        return expiration;
    }

    public static String getJwtId() {
        return jwtId;
    }

    public static String getJwtSecret() {
        return jwtSecret;
    }
}

复制代码
```

### 添加代码生成器的主类

```java
package cuit.pymjl.util;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/28 19:16
 **/
public class CodeGenerator {
    // 数据库URL，注意将URL改成你自己对应的数据库名称
    private final static String URL = "jdbc:mysql://127.0.0.1:3306/security?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&allowPublicKeyRetrieval=true";
    //数据库用户名
    private final static String USER_NAME = "root";
    //数据库密码
    private final static String PASSWORD = "xxxxxx";
    //注意将这个参数改成你自己项目的目录
    private final static String OUT_PUT_DIR = "C:\Users\Admin\JavaProjects\blog-code-demo\security\src\main\java";

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USER_NAME, PASSWORD)
                // 全局配置
                .globalConfig((scanner, builder) -> builder.author(scanner.apply("请输入作者名称？")).fileOverride()
                        .outputDir(OUT_PUT_DIR)
                        .fileOverride())
                // 包配置
                .packageConfig((scanner, builder) -> builder.parent(scanner.apply("请输入包名？")))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .addTablePrefix("t_")
                        .controllerBuilder().enableRestStyle().enableHyphenStyle()
                        .entityBuilder().enableLombok().addTableFills(
                                new Column("create_time", FieldFill.INSERT)
                        ).build())
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
                .execute();

    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}

复制代码
```

然后运行代码生成器，生成项目基本结构

### 开始配置Spring Security

因为篇幅有限，我直接介绍如何集成Spring Security，至于Spring Security的一些组件原理，运行机制我不再赘述，如果对此还不了解的小伙伴可自行上网百度，或者参考以下文章：

[Spring Security 工作原理概览](https://link.juejin.cn?target=https%3A%2F%2Fblog.csdn.net%2Fu012702547%2Farticle%2Fdetails%2F89629415)

[一文带你了解强大的 Spring Security 架构原理！](https://link.juejin.cn?target=https%3A%2F%2Fzhuanlan.zhihu.com%2Fp%2F100014456)

[深入了解Spring Security的实现原理](https://link.juejin.cn?target=https%3A%2F%2Fzhuanlan.zhihu.com%2Fp%2F72305502)

1. 添加RestfulAccessDeniedHandler

```java
/**
 * 当访问接口没有权限时，自定义的返回结果
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/8/20 16:38
 **/
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.toJsonPrettyStr(ResultUtils.fail(ResultEnum.PERMISSION_DENIED)));
        response.getWriter().flush();
    }
}
复制代码
```

1. 添加RestAuthenticationEntryPoint

```java
/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/8/20 16:42
 **/
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(ResultUtils.fail(ResultEnum.AUTHENTICATION_FAILED)));
        response.getWriter().flush();
    }
}
复制代码
```

1. 添加MyUserDetails

```typescript
/**
 * Spring Security需要的用户详情
 * 
 * @author Pymjl
 * @version 1.0
 * @date 2022/8/20 17:19
 **/
public class MyUserDetails implements UserDetails {
    private User user;
    private List<Resource> permissions;

    public MyUserDetails(User user, List<Resource> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的权限
        return permissions.stream()
                .filter(permission -> permission.getValue() != null)
                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
复制代码
```

1. 添加JwtAuthenticationTokenFilter

```java
/**
 * 自定义的JWT验证逻辑
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/8/20 17:24
 **/
@Log4j2
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    RedisService redisService;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // 从请求头中获取token
        String authHeader = request.getHeader(this.tokenHeader);
        // 截取token
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            // token前面的"Bearer "需要截取
            String authToken = authHeader.substring(this.tokenHead.length());
            System.out.println("authToken:" + authToken);
            //验证token,获取token中的username
            Claims claims = JwtUtils.verifyJwt(authToken);
            if (claims == null) {
                throw new AppException("token异常，请重新登录");
            }
            String username = claims.get("username", String.class);
            // 校验该token是否过期
            log.info("username:{}", username);
            String redisToken = (String) redisService.get(username);
            if (redisToken == null) {
                throw new AppException("token已经过期，请重新登录");
            }
            log.info("token verification succeeded, checking username:{}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user:{}", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
复制代码
```

1. 最后，添加Spring Security的配置类

```java
/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/8/20 16:27
 **/
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IUserService userService;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 由于使用的是JWT，我们这里不需要csrf
        httpSecurity.csrf()
                .disable()
                // 基于token，所以不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 允许对于网站静态资源的无授权访问
                .antMatchers(HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                )
                .permitAll()
                // 对登录注册要允许匿名访问
                .antMatchers("/user/login", "/user/register")
                .permitAll()
                //跨域请求会先进行一次options请求
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
//                .antMatchers("/**")//测试时全部运行访问
//                .permitAll();
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> {
            //先获取用户
            User user = userService.getUserByUsername(username);
            if (user != null) {
                List<Resource> permissionList = userService.getUserPermission(user.getId());
                return new MyUserDetails(user, permissionList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
复制代码
```

**相关依赖及方法说明：**

- configure(HttpSecurity httpSecurity)：用于配置需要拦截的url路径、jwt过滤器及出异常后的处理器；
- configure(AuthenticationManagerBuilder auth)：用于配置UserDetailsService及PasswordEncoder；
- RestfulAccessDeniedHandler：当用户没有访问权限时的处理器，用于返回JSON格式的处理结果；
- RestAuthenticationEntryPoint：当未登录或token失效时，返回JSON格式的结果；
- UserDetailsService:SpringSecurity定义的核心接口，用于根据用户名获取用户信息，需要自行实现；
- UserDetails：SpringSecurity定义用于封装用户信息的类（主要是用户信息和权限），需要自行实现；
- PasswordEncoder：SpringSecurity定义的用于对密码进行编码及比对的接口，目前使用的是BCryptPasswordEncoder；
- JwtAuthenticationTokenFilter：在用户名和密码校验前添加的过滤器，如果有jwt的token，会自行根据token信息进行登录。

> 最新版本的Spring Security已经声明继承WebSecurityConfigurerAdapter来进行配置的方法已经过时了

## 进行业务编码

### Servcie层

1. IUserService

在此之前，我们已经用MyBatis Plus的代码生成器生成了项目的基本结构，我们在配置Spring Security时用到了IUserService的一些方法，下面我们来实现这些业务代码

```php
/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Pymjl
 * @since 2022-08-20
 */
public interface IUserService extends IService<User> {
    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return {@code User}
     */
    User getUserByUsername(String username);

    /**
     * 得到当前用户的权限列表
     *
     * @param userId 用户id
     * @return {@code List<Resource>}
     */
    List<Resource> getUserPermission(Long userId);

    /**
     * 用户注册
     *
     * @param userDTO 用户dto
     * @return boolean
     */
    boolean registry(UserDTO userDTO);

    /**
     * 登录,测试demo，就没加验证码
     *
     * @param username 用户名
     * @param password 密码
     * @return {@code String}
     */
    String login(String username, String password);
复制代码
```

1. 实现类

```typescript
/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Pymjl
 * @since 2022-08-20
 */
@Service
@Log4j2
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RedisService redisService;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public List<Resource> getUserPermission(Long userId) {
        List<Roles> roles = baseMapper.getUserRoles(userId);
        List<Resource> results = new ArrayList<>();
        for (Roles role : roles) {
            List<Resource> resources = baseMapper.getUserResources(role.getId());
            results.addAll(resources);
        }
        return results;
    }

    @Override
    public boolean registry(UserDTO userDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, userDTO.getUsername());
        User userInfo = baseMapper.selectOne(wrapper);
        if (userInfo != null) {
            throw new AppException("用户名已存在");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        return baseMapper.insert(user) > 0;
    }

    @Override
    public String login(String username, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = JwtUtils.generateToken(userDetails.getUsername());
            redisService.set(username, token, JwtUtils.getExpiration());
            return token;
        } catch (AuthenticationException e) {
            log.error("登录失败：{}", e.getMessage());
            throw new AppException("登录失败");
        }
    }
}
复制代码
```

### Controller层

UserController

```less
package cuit.epoch.pymjl.security.controller;


import cuit.epoch.pymjl.security.common.result.CommonResult;
import cuit.epoch.pymjl.security.common.result.ResultUtils;
import cuit.epoch.pymjl.security.entity.dto.UserDTO;
import cuit.epoch.pymjl.security.service.IUserService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Pymjl
 * @since 2022-08-20
 */
@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public CommonResult<String> registry(@Valid @RequestBody UserDTO userDTO) {
        userService.registry(userDTO);
        return ResultUtils.success();
    }

    @PostMapping("/login")
    public CommonResult<String> login(@NotBlank(message = "用户名不能为空") @RequestParam String username,
                                      @Length(min = 6, max = 255, message = "密码长度不能小于6位") @RequestParam String password) {
        return ResultUtils.success(userService.login(username, password));
    }

    @GetMapping("/test")
    @PreAuthorize("hasAuthority('admin:test')")
    public CommonResult<String> test() {
        return ResultUtils.success("test");
    }
}
复制代码
```

## 运行测试

1. 启动项目

注意，你需要在你的pom文件里面加入如下代码，否则Spring Boot扫描不到你的mapper.xml

```xml
<build>
        <resources>
            <resource>
                <!-- xml放在java目录下-->
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <!--指定资源的位置（xml放在resources下，可以不用指定）-->
            <resource>
                <directory>src/main/resources</directory>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.3.9.RELEASE</version>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
复制代码
```

![image-20220822142708722](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/63951f71418049f1ab8025f5e89c0ef2~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

如此，可以看到，项目已经启动起来了

1. 打开apifox进行操作

![image-20220822142910282](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/43e3e75366c448fc8bbd461335f5bf42~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

我们先注册一个用户，然后在数据库中手动进行权限的赋值（因为我没写对应的接口）

- 先在t_roles中创建一个如图所示的角色

![image-20220822143047951](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/6395655bc01b43a78ba348768afd0cc9~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

- 然后将刚才注册的用户与角色进行关联（t_user_role）

![image-20220822143123217](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8c09e10504ba491c9d9ef9118c94ba51~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

- 在t_resource中添加如图所示的权限

![image-20220822143218337](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/fc389a79c1904b4cbdb52397186560d7~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

- 然后将对应的权限与角色admin进行关联

![image-20220822143251620](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4551e8b671ac402eab33aaec55077c07~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

接下来我们进行登录操作，登陆成功后会返回对应的token

![image-20220822143436731](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8ffcc1bd698641c98da9ff8ad62e20a8~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

我们将token放在header中，对test接口进行测试

![image-20220822143532069](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5a0f748d94a24059bb9112439d4a3d46~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

我们可以看到，因为先前我们对用户进行了权限的赋值，所以当前用户访问test接口是没有问题的，接下来我们重新注册一个账号，然后对他不赋予权限的访问test接口，我们再看看结果如何

![image-20220822143722955](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ca043496ca424109a84cda6888961111~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

![image-20220822143740344](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/af3fdfbee3134b688692b330f67538f9~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

![image-20220822143829669](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e0468e13ca0643a5a347496d1a966efe~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

我们可以看到，pl账户是没有权限访问test接口的，后台日志抛出了如图所示的异常

![image-20220822143927474](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a47465baf36840f698f40ddd4963cb77~tplv-k3u1fbpfcp-zoom-in-crop-mark:3024:0:0:0.awebp)

如此我们就完成了集成Spring Security实现接口级别的认证和权限控制了。

