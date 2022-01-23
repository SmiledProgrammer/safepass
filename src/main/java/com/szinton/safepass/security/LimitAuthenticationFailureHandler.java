package com.szinton.safepass.security;

import com.szinton.safepass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LimitAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String FAILURE_URL = "/login-error";

    private final UserService userService;

    public LimitAuthenticationFailureHandler(UserService userService) {
        super(FAILURE_URL);
        this.userService = userService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        super.onAuthenticationFailure(request, response, ex);
    }
}
