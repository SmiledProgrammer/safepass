package com.szinton.safepass.service;

import com.szinton.safepass.domain.Password;
import com.szinton.safepass.domain.User;
import com.szinton.safepass.dto.PasswordDto;
import com.szinton.safepass.dto.ServicePasswordDto;
import com.szinton.safepass.repo.PasswordRepository;
import com.szinton.safepass.security.PasswordEncryptionAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;
    private final UserService userService;
    private final PasswordEncryptionAlgorithm encryptionAlgorithm;

    @Override
    public void savePassword(String username, String masterPassword, PasswordDto savedPassword) {
        User user = getUserWithVerifiedMasterPassword(username, masterPassword);
        Long userId = user.getId();
        Password emptyRecord = passwordRepository.findByUserIdAndServiceName(userId, savedPassword.getServiceName());
        if (emptyRecord != null) {
            throw new IllegalArgumentException("There is already a password for this service.");
        }
        String encryptedPassword = encryptionAlgorithm.encrypt(savedPassword.getPassword(), masterPassword);
        Password newRecord = new Password(savedPassword.getServiceName(), encryptedPassword, userId);
        passwordRepository.save(newRecord);
    }

    @Override
    public ServicePasswordDto getPassword(String username, String masterPassword, String serviceName) {
        User user = getUserWithVerifiedMasterPassword(username, masterPassword);
        Long userId = user.getId();
        Password record = getPasswordRecord(userId, serviceName);
        String password = encryptionAlgorithm.decrypt(record.getPassword(), masterPassword);
        return new ServicePasswordDto(password);
    }

    @Override
    public List<String> getPasswordsServices(String username) {
        Long userId = userService.getUser(username).getId();
        List<Password> records = passwordRepository.findByUserId(userId);
        return records.stream()
                .map(Password::getServiceName)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePassword(String username, String masterPassword, PasswordDto updatedPassword) {
        User user = getUserWithVerifiedMasterPassword(username, masterPassword);
        Long userId = user.getId();
        Password record = getPasswordRecord(userId, updatedPassword.getServiceName());
        String encryptedPassword = encryptionAlgorithm.encrypt(updatedPassword.getPassword(), masterPassword);
        record.setPassword(encryptedPassword);
        passwordRepository.save(record);
    }

    @Override
    public void deletePassword(String username, String serviceName) {
        Long userId = userService.getUser(username).getId();
        Password record = passwordRepository.findByUserIdAndServiceName(userId, serviceName);
        if (record != null) {
            if (!Objects.equals(userId, record.getUserId())) {
                throw new AccessDeniedException("Users identifiers mismatching.");
            }
            passwordRepository.delete(record);
        }
    }

    private Password getPasswordRecord(Long userId, String serviceName) {
        Password record = passwordRepository.findByUserIdAndServiceName(userId, serviceName);
        if (record == null) {
            throw new IllegalArgumentException("No password for the specified service found.");
        }
        if (!Objects.equals(userId, record.getUserId())) {
            throw new AccessDeniedException("Users identifiers mismatching.");
        }
        return record;
    }

    private User getUserWithVerifiedMasterPassword(String username, String masterPassword) {
        User user = userService.getUser(username);
        String actualUsername = user.getUsername();
        String decryptedUsername = encryptionAlgorithm.decrypt(user.getEncryptedUsername(), masterPassword);
        if (!actualUsername.equals(decryptedUsername)) {
            throw new AccessDeniedException("Incorrect master password.");
        }
        return user;
    }
}
