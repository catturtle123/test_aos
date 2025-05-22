package com.umc.authentication.security.util;

import com.umc.authentication.apiPayload.exception.AuthException;
import com.umc.authentication.security.principal.PrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.umc.authentication.apiPayload.code.status.ErrorStatus.*;

@Slf4j
@Component
@Getter
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long accessTokenValiditySec;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public JwtUtil(
            @Value("${spring.jwt.secret}") final String secretKey,
            @Value("${spring.jwt.access-token-validity}") final Long accessTokenValiditySec) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValiditySec = accessTokenValiditySec;
    }

    public String createAccessToken(PrincipalDetails principalDetails) {
        return createToken(principalDetails, accessTokenValiditySec);
    }

    private String createToken(
            PrincipalDetails principalDetails, Long validitySeconds) {
        Instant issuedAt = Instant.now();
        Instant expirationTime = issuedAt.plusSeconds(validitySeconds);

        return Jwts.builder()
                .setSubject(String.valueOf(principalDetails.getUserId()))
                .claim("role", principalDetails.getAuthorities())
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String token) {
        return getClaims(token).getBody().getSubject();
    }

    public Long getExpiration(String token) {
        return getClaims(token).getBody().getExpiration().getTime();
    }

    public boolean isTokenValid(String token) throws AuthException {
        try {
            Jws<Claims> claims = getClaims(token);
            Date expiredDate = claims.getBody().getExpiration();
            Date now = new Date();
            return expiredDate.after(now);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 JWT 형식
            throw new AuthException(AUTH_UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException e) {
            // JWT 형식이 잘못된 경우 (구조적 문제)
            throw new AuthException(AUTH_MALFORMED_TOKEN);
        } catch (io.jsonwebtoken.security.SignatureException e) {
            // JWT 서명 검증 실패 (JJWT 라이브러리)
            log.error("JWT signature validation failed: {}", e.getMessage());
            throw new AuthException(AUTH_SIGNATURE_INVALID);
        } catch (SecurityException e) {
            // 일반적인 보안 예외
            throw new AuthException(AUTH_SIGNATURE_INVALID);
        } catch (IllegalArgumentException e) {
            // 토큰이 null이거나 빈 문자열인 경우
            throw new AuthException(AUTH_TOKEN_EMPTY);
        } catch (JwtException e) {
            // 기타 JJWT 라이브러리 관련 예외
            log.error("JWT processing error: {}", e.getMessage(), e);
            throw new AuthException(AUTH_INVALID_TOKEN);
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            log.error("Unexpected JWT validation error: {}", e.getMessage(), e);
            throw new AuthException(AUTH_TOKEN_PROCESSING_ERROR);
        }
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication (String token) {
        Claims claims = getClaims(token).getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
