package com.list.todo.config.jwt;

import com.list.todo.common.ErrorCode;
import com.list.todo.common.GlobalException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final long ACCESS_TOKEN_EXPIRES_IN = 1000 * 60 * 60; // 1시간
    private static final long REFRESH_TOKEN_EXPIRES_IN = 1000 * 60 * 60 * 24 * 7;   // 7일
    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDTO generate(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        String accessToken = Jwts.builder()
                .claim("roles", authorities)
                .setSubject(authentication.getName())
                .setIssuer("todo app")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRES_IN))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .claim("roles", authorities)
                .setSubject(authentication.getName())
                .setIssuer("todo app")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRES_IN))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new TokenDTO(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if (claims.get("roles") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 토큰입니다.", e);
            throw new GlobalException(ErrorCode.ACCESS_TOKEN_NOT_VALIDATE);
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰입니다.", e);
            throw new GlobalException(ErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 토큰입니다.", e);
            throw new GlobalException(ErrorCode.ACCESS_TOKEN_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            log.info("JWT 클레임 문자열이 비었습니다.", e);
            throw new GlobalException(ErrorCode.ACCESS_TOKEN_CLAIM_EMPTY);
        }
    }

    public Long getExpirationTime(String accessToken) {
        Claims claims = parseClaims(accessToken);

        return claims.getExpiration().getTime();
    }

}
