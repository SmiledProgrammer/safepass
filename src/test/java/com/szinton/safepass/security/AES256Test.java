package com.szinton.safepass.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AES256Test {

    @Test
    void encryptionDecryption() {
        PasswordEncryptionAlgorithm aes = new AES256();
        String text = "Secret Password";
        String secretKey = "Secret Key";

        String cipherText = aes.encrypt(text, secretKey);
        String decryptedText = aes.decrypt(cipherText, secretKey);

        assertEquals(text, decryptedText);
    }
}
