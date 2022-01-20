package com.szinton.safepass;

import com.szinton.safepass.security.AES256;
import com.szinton.safepass.security.PasswordEncryptionAlgorithm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SafepassApplication {

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
    PasswordEncryptionAlgorithm passwordEncrypter() { return new AES256(); }
}
