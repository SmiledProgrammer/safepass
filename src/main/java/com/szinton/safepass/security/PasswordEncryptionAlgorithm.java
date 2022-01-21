package com.szinton.safepass.security;

public interface PasswordEncryptionAlgorithm {

    String encrypt(String plainText, String secretKey);

    String decrypt(String cipherText, String secretKey);
}
