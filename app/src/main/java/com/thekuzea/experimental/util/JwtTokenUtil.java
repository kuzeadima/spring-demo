package com.thekuzea.experimental.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.thekuzea.experimental.config.jwt.JwtRequestFilter;

import static com.thekuzea.experimental.util.DateTimeUtil.convertDateToLocalDateTime;
import static com.thekuzea.experimental.util.DateTimeUtil.convertLocalDateTimeToDate;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${security.settings.jwt.validity}")
    private Integer jwtTokenValidityHoursAmount;

    @Value("${security.settings.jwt.secret}")
    private String secret;

    public String getUsernameFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(convertLocalDateTimeToDate(LocalDateTime.now()))
                .setExpiration(convertLocalDateTimeToDate(LocalDateTime.now().plusHours(jwtTokenValidityHoursAmount)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace(JwtRequestFilter.BEARER, StringUtils.EMPTY))
                .getBody();
    }

    private <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Boolean isTokenExpired(final String token) {
        final LocalDateTime expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(LocalDateTime.now());
    }

    private LocalDateTime getExpirationDateFromToken(final String token) {
        return convertDateToLocalDateTime(getClaimFromToken(token, Claims::getExpiration));
    }
}
