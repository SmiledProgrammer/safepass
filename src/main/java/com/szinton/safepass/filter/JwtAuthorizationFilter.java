package com.szinton.safepass.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (Set.of("/api/login", "/api/users").contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
                try {
                    String jwt = authorizationHeader.substring(TOKEN_PREFIX.length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // TODO: create util class to store this
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(jwt);
                    String username = decodedJWT.getSubject();
                    Collection<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("default"));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info("Authorization successful.");
                    filterChain.doFilter(request, response);
                } catch (Exception ex) {
                    // TODO: add complex exception handling as apparently all exceptions are coming here
                    log.error("Error during authorization: {}", ex.getMessage()); // TODO: add token expiration handling
                    response.sendError(FORBIDDEN.value());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
