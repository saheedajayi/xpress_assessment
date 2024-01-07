package com.solution.xpresss_assessment.Utilities;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.solution.xpresss_assessment.Utilities.Constants.ISSUER;


@Component
@RequiredArgsConstructor
public class JwtUtils {
    @Value("${Jwt_Secret_Key}")
    private String jwtSecretKey;
    @Value("${access_expiration}")
    private long accessExpiration;
    @Value("${refresh_expiration}")
    private long refreshExpiration;


    private  byte[] signKey() {
        return TextCodec.BASE64.decode(jwtSecretKey);
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(signKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateAccessToken(
            Map<String, Object> claims,
            String email
    ) {
        return generateToken(claims, email, accessExpiration);
    }

    public String generateRefreshToken(String email) {
        return generateToken(new HashMap<>(), email, refreshExpiration);
    }

    private String generateToken(
            Map<String, Object> claims,
            String email,
            long expirationTime
    ) {
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(Instant.now()))
                .setClaims(claims)
                .setExpiration(Date.from(
                        Instant.now()
                                .plusSeconds(
                                        expirationTime)))
                .signWith(SignatureAlgorithm.HS512, signKey())
                .setSubject(email)
                .compact();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(signKey())
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
