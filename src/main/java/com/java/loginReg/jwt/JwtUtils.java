package com.java.loginReg.jwt;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final String jwtSecret = "HealSyncSecretKey";  // 🔐 Store securely in prod
    private final long jwtExpirationMs = 86400000; // 24 hours

    // ✅ Generate token with email (username)
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // ✅ Get username (email) from token
    public String getUsernameFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // ✅ Validate token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }

        return false;
    }

    // 🔍 Internal claim extractor
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
 // ✅ Validate token against user details
    public boolean validateJwtToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = getUsernameFromJwtToken(token);
        return (username.equals(userDetails.getUsername()) && validateJwtToken(token));
    }

}
