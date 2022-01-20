package com.szinton.safepass.security;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

@Slf4j
public class AES256 implements PasswordEncryptionAlgorithm {
    private static final String SALT = "salt"; // TODO: store securely
    private static final byte[] IV = { -73, -108, -28, 5, 46, -75, 71, 15, -26, -61, -93, -57, -11, 51, -58, -124 }; // TODO: store securely

    @Override
    public String encrypt(String plainText, String secretKey) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(IV);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmpKey = keyFactory.generateSecret(keySpec);
            SecretKeySpec key = new SecretKeySpec(tmpKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(ENCRYPT_MODE, key, ivSpec);
            byte[] cipheredBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipheredBytes);
        } catch (Exception ex) {
            log.error("Error during AES256 encryption: {}", ex.getMessage());
        }
        return null;
    }

    @Override
    public String decrypt(String cipheredText, String secretKey) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(IV);

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), SALT.getBytes(), 65536, 256);
            SecretKey tmpKey = keyFactory.generateSecret(keySpec);
            SecretKeySpec key = new SecretKeySpec(tmpKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(DECRYPT_MODE, key, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipheredText));
            return new String(decryptedBytes);
        } catch (Exception ex) {
            log.error("Error during AES256 decryption: {}", ex.getMessage());
        }
        return null;
    }
}
