package com.szinton.safepass.security;

import com.auth0.jwt.algorithms.Algorithm;
import com.szinton.safepass.filter.JwtAuthenticationFilter;
import com.szinton.safepass.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Algorithm jwtAlgorithm;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final LimitAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean());
        authenticationFilter.setFilterProcessesUrl("/api/login");
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        http.csrf().disable();

        http.headers().xssProtection();
        http.headers().contentSecurityPolicy("script-src 'self'");
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.authorizeRequests().antMatchers("/api/login", "/api/users", "/h2/**").permitAll(); // TODO: remove /h2
        http.authorizeRequests().antMatchers("/register", "/master-password", "/login", "/login-error", "/vault", "/error").permitAll();
//        http.authorizeRequests().anyRequest().authenticated(); // TODO: uncomment

        http.addFilter(authenticationFilter);
        http.addFilterBefore(new JwtAuthorizationFilter(jwtAlgorithm), UsernamePasswordAuthenticationFilter.class);

        http.formLogin().loginPage("/login").loginProcessingUrl("/api/login");
        http.logout().logoutSuccessUrl("/login");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
