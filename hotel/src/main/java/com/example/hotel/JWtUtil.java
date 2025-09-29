package com.example.hotel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
/*@Component: 表示这是一个 Spring 组件，会被 Spring 容器管理，可以在其他地方通过 @Autowired*/
@Component
public class JWtUtil {
//    private final SecretKey key= Keys.secretKeyFor(SignatureAlgorithm.HS256);// 使用 HS256 算法生成的密钥
    private final long time=1000*60*60*24;//定义token的有效时间
    private final SecretKey key;
    public JWtUtil(@Value("${jwt.secret:default-secret-key}") String secretKey) {
        // 确保密钥长度足够（至少32字节）
        String paddedSecret = padSecretKey(secretKey);
        byte[] keyBytes = Base64.getDecoder().decode(paddedSecret);
        this.key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }
    // 确保密钥长度符合要求
    private String padSecretKey(String secretKey) {
        // 如果密钥太短，填充到32字节
        if (secretKey.length() < 32) {
            StringBuilder sb = new StringBuilder(secretKey);
            while (sb.length() < 32) {
                sb.append("0");
            }
            return Base64.getEncoder().encodeToString(sb.toString().getBytes());
        }
        return secretKey;
    }
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
