package com.epam.esm.jwt;

import com.epam.esm.exception.ServiceExceptionCode;
import com.epam.esm.service.IUserService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.epam.esm.exception.ServiceExceptionCode.EXPIRED_OR_INVALID_JWT_TOKEN;

@Slf4j
@Component
public class JwtTokenProvider {
    public static final String HEADER_NAME = "Authorization";
    public static final String HEADER_PREFIX = "Bearer ";
    public static final String CLAIM_ROLE = "role";

    @Autowired
    private IUserService userService;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(CLAIM_ROLE, roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setId(UUID.randomUUID().toString())
                   .setExpiration(validity)
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }

    public String createRefreshToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(CLAIM_ROLE, roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds * 12);
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setId(UUID.randomUUID().toString())
                   .setExpiration(validity)
                   .signWith(SignatureAlgorithm.HS256, secretKey)
                   .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(HEADER_NAME);
        if (bearerToken != null && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            ServiceExceptionCode exception = EXPIRED_OR_INVALID_JWT_TOKEN;
            log.error(exception.getExceptionCode() + ":" + e.getLocalizedMessage());
            return false;
        }
    }
}
