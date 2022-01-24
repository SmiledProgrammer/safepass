package com.szinton.safepass.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String SUCCESS_URL = "/vault";

    private final Algorithm jwtAlgorithm;

    public AuthenticationSuccessHandler(Algorithm jwtAlgorithm) {
        super(SUCCESS_URL);
        super.setAlwaysUseDefaultTargetUrl(true);
        this.jwtAlgorithm = jwtAlgorithm;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(jwtAlgorithm);
        response.setHeader("Access-Token", accessToken);
//        super.onAuthenticationSuccess(request, response, authentication);
    }
}
