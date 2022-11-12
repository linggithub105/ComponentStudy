package com.itjing.security.jwt;

import com.itjing.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author lijing
 * @date 2022年06月09日 19:11
 * @description Jwt工具类
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenUtil {

    /**
     * 携带JWT令牌的HTTP的Header的名称，在实际生产中可读性越差越安全
     */
    @Getter
    @Value("${jwt.header:Authorization}")
    private String header;

    /**
     * 为JWT基础信息加密和解密的密钥
     * 在实际生产中通常不直接写在配置文件里面。而是通过应用的启动参数传递，并且需要定期修改。
     */
    @Value("${jwt.secret:default}")
    private String secret;

    /**
     * JWT令牌的有效时间，单位秒，默认2周
     */
    @Value("${jwt.expiration:1209600}")
    private Long expiration;

    /**
     * default SHA_256 secretKey flag
     */
    private static final String DEFAULT_SECRET_FLAG = "default";

    /**
     * minimum SHA_256 secretKey string length
     */
    private static final int SHA_256_SECRET_CHAR_SIZE = 256 / 8;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * SecretKey 根据 SECRET 的编码方式解码后得到：
     * Base64 编码：SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
     * Base64URL 编码：SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretString));
     * 未编码：SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
     */
    private static SecretKey getSecretKey(String secret) {
        // use default secretKey for SHA-256
        if (secret == null || DEFAULT_SECRET_FLAG.equals(secret)) {
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            // use custom secretKey
            int size = secret.length();
            int left = SHA_256_SECRET_CHAR_SIZE - size;
            if (left > 0) {
                // character for padding
                StringBuilder stringBuilder = new StringBuilder(secret);
                for (int i = 0; i < left; i++) {
                    stringBuilder.append(i % 10);
                }
                return Keys.hmacShaKeyFor(stringBuilder.toString().getBytes());
            } else {
                return Keys.hmacShaKeyFor(secret.getBytes());
            }
        }
        /*byte[] encodeKey = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(encodeKey);*/
    }

    /**
     * 用claims生成token
     *
     * @param claims 数据声明，用来创建payload的私有声明
     * @return token 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        SecretKey key = getSecretKey(secret);
        // SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //两种方式等价
        // 添加payload声明
        JwtBuilder jwtBuilder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setId(UUID.randomUUID().toString())
                // iat: jwt的签发时间
                .setIssuedAt(new Date())
                // 你也可以改用你喜欢的算法，支持的算法详见：https://github.com/jwtk/jjwt#features
                // SignatureAlgorithm.HS256：指定签名的时候使用的签名算法，也就是header那部分
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(new Date(System.currentTimeMillis() + this.expiration * 1000));
        String token = "Bearer " + jwtBuilder.compact();
        return token;
    }

    /**
     * 生成Token令牌
     *
     * @param userDetails 用户
     * @return 令牌Token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userDetails.getUsername());
        claims.put("created", new Date());
        return generateToken(claims);
    }

    /**
     * 生成token
     *
     * @param userName
     * @return
     */
    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userName);
        claims.put("created", new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取数据声明claim
     *
     * @param token 令牌token
     * @return 数据声明claim
     */
    public Claims getClaimsFromToken(String token) {
        try {
            SecretKey key = getSecretKey(secret);
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
            return null;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
            return null;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token." + e.getMessage());
            log.trace("Invalid JWT token trace: {}", e);
            return null;
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
            return null;
        }
    }

    public String getUserRole(String token) {
        return (String) getClaimsFromToken(token).get("role");
    }

    /**
     * 从token中获取登录用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getSubjectFromToken(String token) {
        String subject;
        try {
            Claims claims = getClaimsFromToken(token);
            subject = claims.getSubject();
        } catch (Exception e) {
            subject = null;
        }
        return subject;
    }


    /**
     * 获取token的过期时间
     *
     * @param token token
     * @return 过期时间
     */
    public Date getExpirationFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 判断token是否过期
     *
     * @param token 令牌
     * @return 是否过期：已过期返回true，未过期返回false
     */
    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 验证令牌：判断token是否非法
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 如果token未过期且合法，返回true，否则返回false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        // 如果已经过期返回false
        if (isTokenExpired(token)) {
            return false;
        }
        String usernameFromToken = getSubjectFromToken(token);
        String username = userDetails.getUsername();
        return username.equals(usernameFromToken);
    }

    /**
     * 获取认证信息
     *
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = userDetailsService.loadUserByUsername((String) claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
