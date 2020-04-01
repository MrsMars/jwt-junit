package com.aoher.security.filter;

import com.aoher.security.model.CustomUserPrincipal;
import com.aoher.security.util.JwtUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.aoher.utis.SecurityConstants.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl(AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            JavaType stringType = mapper.getTypeFactory().constructType(String.class);
            JavaType type = mapper.getTypeFactory().constructMapType(Map.class, stringType, stringType);

            Map<String, String> data = mapper.readValue(request.getInputStream(), type);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            data.get("email"),
                            data.get("password")
                    )
            );
        } catch (IOException e) {
            log.error("IOException => {}", e.getMessage());
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication){
        CustomUserPrincipal principal = ((CustomUserPrincipal) authentication.getPrincipal());
        String token = JwtUtils.generateJwt(principal.getEmployee());
        response.addHeader(TOKEN_HEADER, TOKEN_PREFIX + token);
    }
}
