package com.project.shopappbaby.components;

import com.project.shopappbaby.exceptions.InvalidParamException;
import com.project.shopappbaby.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration; //lưu vào biến môi trường
    @Value("${jwt.secretKey}")
    private String secretKey;
    public String generateToken(com.project.shopappbaby.models.User user) throws Exception{
        //thuộc tính => claims
        Map<String, Object> claims = new HashMap<>();
        //this.generateSecretKey();
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) //token phải có secret key, từ secret key đưa về các claim -thuật toán mã hóa
                    .compact();
            return token;
        }catch (Exception e) {
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
            // chuần hóa đối tượng user thành đối tượng user trong spring security
        }
    }
    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        //Keys.hmacShaKeyFor(Decoders.BASE64.decode("Q+aq8M9WeXUPX01waMih68w2Ko+H/ArAahBciS8ZaSM="));
        return Keys.hmacShaKeyFor(bytes); // chuyển đổi từ secretkey dạng String sang secretkey dang key
    }
    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
    private Claims extractAllClaims(String token) {// từ các token extract ra các claim
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // trả về danh sách các claim
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //kiểm tra  expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());// xem token có nằm trước ngày quá hạn hay không
    }
    // trích xuất phoneNumber từ Token, trong đó thì phoneNumber đóng vbai trò là object
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }
}
