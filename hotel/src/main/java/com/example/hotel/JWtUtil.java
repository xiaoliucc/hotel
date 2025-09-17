package com.example.hotel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
/*@Component: 表示这是一个 Spring 组件，会被 Spring 容器管理，可以在其他地方通过 @Autowired*/
@Component
public class JWtUtil {
    private final SecretKey key= Keys.secretKeyFor(SignatureAlgorithm.HS256);// 使用 HS256 算法生成的密钥
    private final long time=1000*60*60*24;//定义token的有效时间

    public String generateToken(Long usrId,String role){
        return Jwts.builder()
                .setSubject(usrId.toString())
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .signWith(key)
                .compact();
    }
    /*解密*/
    public Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /*判断Token是否过期*/
    public boolean isExpired(String token){
        return parseToken(token).getExpiration().before(new Date());
    }
}
