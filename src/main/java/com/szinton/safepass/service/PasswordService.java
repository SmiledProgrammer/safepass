package com.szinton.safepass.service;

import com.szinton.safepass.dto.PasswordDto;

import java.util.List;

public interface PasswordService {

    void savePassword(String username, String masterPassword, PasswordDto savedPassword);

    String getPassword(String username, String masterPassword, String serviceName);

    List<String> getPasswordsServices(String username);

    void updatePassword(String username, String masterPassword, PasswordDto updatedPassword);

    void deletePassword(String username, String serviceName);
}
