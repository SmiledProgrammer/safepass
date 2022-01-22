package com.szinton.safepass;

import com.auth0.jwt.algorithms.Algorithm;
import com.szinton.safepass.security.AES256;
import com.szinton.safepass.security.PasswordEncryptionAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class SafepassApplication {

    private final Environment env;

    public static void main(String[] args) {
        SpringApplication.run(SafepassApplication.class, args);
    }

    // Used for users accounts passwords
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Used for passwords users store in the database
    @Bean
    PasswordEncryptionAlgorithm passwordEncrypter() { return new AES256(env.getProperty("AES256_SALT")); }

    // Used for JWT verification
    @Bean
    Algorithm jwtAlgorithm() { return Algorithm.HMAC256(env.getProperty("JWT_SECRET").getBytes()); }
}
