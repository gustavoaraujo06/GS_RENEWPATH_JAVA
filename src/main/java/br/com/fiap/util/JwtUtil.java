package br.com.fiap.util;

import java.security.Key;
import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    private static final String SECRET_KEY = "668f16d81f917a345316c08cb4da91b7ad83b015a4bb8557488766f161ec5f5f";

    public static String generateToken(String id, int role) {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes), SignatureAlgorithm.HS256)
                .compact();
    }
    private static Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public static Claims parseToken(String token) throws Exception {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new Exception("Token inválido ou expirado!", e);
        }
    }
}
