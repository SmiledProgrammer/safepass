package com.szinton.safepass.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.szinton.safepass.dto.PasswordDto;
import com.szinton.safepass.dto.ServicePasswordDto;
import com.szinton.safepass.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;
    private final Algorithm jwtAlgorithm;

    @PostMapping("/passwords")
    public ResponseEntity<Void> savePassword(
            @RequestHeader(name = "Master-Password") @NotNull String masterPassword,
            @RequestHeader(name = "Authorization") @NotNull String authHeader,
            @RequestBody @Valid PasswordDto passwordDto) {
        passwordService.savePassword(getSubjectFromJwtHeader(authHeader), masterPassword, passwordDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/passwords")
    public DeferredResult<ResponseEntity<ServicePasswordDto>> getPassword(
            @RequestHeader(name = "Master-Password") @NotNull String masterPassword,
            @RequestHeader(name = "Authorization") @NotNull String authHeader,
            @RequestParam @NotNull String serviceName) {
        DeferredResult<ResponseEntity<ServicePasswordDto>> output = new DeferredResult<>();
        ForkJoinPool.commonPool().submit(() -> {
            try {
                Thread.sleep(1000);
                ServicePasswordDto password = passwordService.getPassword(getSubjectFromJwtHeader(authHeader), masterPassword, serviceName);
                output.setResult(new ResponseEntity<>(password, OK));
            } catch (Exception ex) {
                output.setErrorResult(ex);
            }
        });
        return output;
    }

    @GetMapping("/passwords/services")
    public ResponseEntity<List<String>> getPasswordsServices(
            @RequestHeader(name = "Authorization") @NotNull String authHeader) {
        List<String> services = passwordService.getPasswordsServices(getSubjectFromJwtHeader(authHeader));
        return new ResponseEntity<>(services, OK);
    }

    @PutMapping("/passwords")
    public DeferredResult<ResponseEntity<Void>> updatePassword(
            @RequestHeader(name = "Master-Password") @NotNull String masterPassword,
            @RequestHeader(name = "Authorization") @NotNull String authHeader,
            @RequestBody @Valid PasswordDto passwordDto) {
        DeferredResult<ResponseEntity<Void>> output = new DeferredResult<>();
        ForkJoinPool.commonPool().submit(() -> {
            try {
                Thread.sleep(1000);
                passwordService.updatePassword(getSubjectFromJwtHeader(authHeader), masterPassword, passwordDto);
                output.setResult(new ResponseEntity<>(OK));
            } catch (Exception ex) {
                output.setErrorResult(ex);
            }
        });
        return output;
    }

    @DeleteMapping("/passwords")
    public ResponseEntity<Void> deletePassword(
            @RequestHeader(name = "Authorization") @NotNull String authHeader,
            @RequestParam @NotNull String serviceName) {
        passwordService.deletePassword(getSubjectFromJwtHeader(authHeader), serviceName);
        return new ResponseEntity<>(OK);
    }

    private String getSubjectFromJwtHeader(String jwtHeader) {
        String jwt = jwtHeader.substring("Bearer ".length());
        JWTVerifier verifier = JWT.require(jwtAlgorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT.getSubject();
    }
}
