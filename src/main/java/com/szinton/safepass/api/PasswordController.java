package com.szinton.safepass.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.szinton.safepass.dto.PasswordDto;
import com.szinton.safepass.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/passwords")
    public ResponseEntity<Void> savePassword(
            @RequestHeader(name = "Master-Password") @NotNull String masterPassword,
            @RequestHeader(name = "Authorization") @NotNull String authHeader,
            @RequestBody @Valid PasswordDto passwordDto) {
        passwordService.savePassword(getSubjectFromJwtHeader(authHeader), masterPassword, passwordDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/passwords")
    public ResponseEntity<String> getPassword(
            @RequestHeader(name = "Master-Password") @NotNull String masterPassword,
            @RequestHeader(name = "Authorization") @NotNull String authHeader,
            @RequestBody @NotNull String serviceName) {
        String password = passwordService.getPassword(getSubjectFromJwtHeader(authHeader), masterPassword, serviceName);
        return new ResponseEntity<>(password, OK);
    }

    @GetMapping("/passwords/services")
    public ResponseEntity<List<String>> getPasswordsServices(
            @RequestHeader(name = "Authorization") @NotNull String authHeader) {
        List<String> services = passwordService.getPasswordsServices(getSubjectFromJwtHeader(authHeader));
        return new ResponseEntity<>(services, OK);
    }

    @PutMapping("/passwords")
    public ResponseEntity<Void> updatePassword(
            @RequestHeader(name = "Master-Password") @NotNull String masterPassword,
            @RequestHeader(name = "Authorization") @NotNull String authHeader,
            @RequestBody @Valid PasswordDto passwordDto) {
        passwordService.updatePassword(getSubjectFromJwtHeader(authHeader), masterPassword, passwordDto);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/passwords")
    public ResponseEntity<Void> deletePassword(
            @RequestHeader(name = "Authorization") @NotNull String authHeader,
            @RequestBody @NotNull String serviceName) {
        passwordService.deletePassword(getSubjectFromJwtHeader(authHeader), serviceName);
        return new ResponseEntity<>(OK);
    }

    private String getSubjectFromJwtHeader(String jwtHeader) {
        String jwt = jwtHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); // TODO: create util class to store this
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getSubject();
    }
}
