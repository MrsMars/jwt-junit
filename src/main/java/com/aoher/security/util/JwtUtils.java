package com.aoher.security.util;

import com.aoher.entities.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.aoher.utis.SecurityConstants.*;

public class JwtUtils {

    public static String generateJwt(Employee user) {
        List<String> roles = new ArrayList<>();
        byte[] signingKey = JWT_SECRET.getBytes();

        String subject = Long.toString(user.getId());
        return Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", TOKEN_TYPE)
                .setIssuer(TOKEN_ISSUER)
                .setAudience(TOKEN_AUDIENCE)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + 864000000)).claim("rol", roles).compact();
    }

    public static UsernamePasswordAuthenticationToken parseJwt(String token) {
        byte[] signingKey = JWT_SECRET.getBytes();
        Jws<Claims> parsedToken = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);

        String employeeId = parsedToken.getBody().getSubject();
        List<SimpleGrantedAuthority> authorities = Collections.emptyList();

        return employeeId == null ? null :
                new UsernamePasswordAuthenticationToken(employeeId, null, authorities);
    }

    private JwtUtils() {
    }
}
