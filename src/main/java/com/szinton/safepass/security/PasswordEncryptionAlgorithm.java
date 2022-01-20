package com.szinton.safepass.security;

public interface PasswordEncryptionAlgorithm {

    String encrypt(String password, String secretKey);

    String decrypt(String encryptedPassword, String secretKey);
}
