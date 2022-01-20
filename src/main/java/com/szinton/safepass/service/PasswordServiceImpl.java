package com.szinton.safepass.service;

import com.szinton.safepass.domain.Password;
import com.szinton.safepass.domain.User;
import com.szinton.safepass.dto.PasswordDto;
import com.szinton.safepass.repo.PasswordRepository;
import com.szinton.safepass.repo.UserRepository;
import com.szinton.safepass.security.PasswordEncryptionAlgorithm;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;
    private final PasswordEncryptionAlgorithm encryptionAlgorithm;

    @Override
    public void savePassword(String username, String masterPassword, PasswordDto savedPassword) {
        Long userId = getUserId(username);
        Password emptyRecord = passwordRepository.findByUserIdAndServiceName(userId, savedPassword.getServiceName());
        if (emptyRecord != null) {
            throw new IllegalArgumentException("There is already a password for this service.");
        }
        String encryptedPassword = encryptionAlgorithm.encrypt(savedPassword.getPassword(), masterPassword);
        Password newRecord = new Password(savedPassword.getServiceName(), encryptedPassword, userId);
        passwordRepository.save(newRecord);
    }

    @Override
    public String getPassword(String username, String masterPassword, String serviceName) {
        Long userId = getUserId(username);
        Password record = getPasswordRecord(userId, serviceName);
        return encryptionAlgorithm.decrypt(record.getPassword(), masterPassword);
    }

    @Override
    public List<String> getPasswordsServices(String username) {
        Long userId = getUserId(username);
        List<Password> records = passwordRepository.findByUserId(userId);
        return records.stream()
                .map(Password::getServiceName)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePassword(String username, String masterPassword, PasswordDto updatedPassword) {
        Long userId = getUserId(username);
        Password record = getPasswordRecord(userId, updatedPassword.getServiceName());
        String encryptedPassword = encryptionAlgorithm.encrypt(updatedPassword.getPassword(), masterPassword);
        record.setPassword(encryptedPassword);
        passwordRepository.save(record);
    }

    @Override
    public void deletePassword(String username, String serviceName) {
        Long userId = getUserId(username);
        Password record = passwordRepository.findByUserIdAndServiceName(userId, serviceName);
        if (record != null) {
            if (!Objects.equals(userId, record.getUserId())) {
                throw new IllegalArgumentException("Users identifiers mismatching.");
            }
            passwordRepository.delete(record);
        }
    }

    private Long getUserId(String username) {
        User record = userRepository.findByUsername(username);
        if (record == null) {
            throw new IllegalArgumentException("User with the given identifier not found.");
        }
        return record.getId();
    }

    private Password getPasswordRecord(Long userId, String serviceName) {
        Password record = passwordRepository.findByUserIdAndServiceName(userId, serviceName);
        if (record == null) {
            throw new IllegalArgumentException("No password for the specified service found.");
        }
        if (!Objects.equals(userId, record.getUserId())) {
            throw new IllegalArgumentException("Users identifiers mismatching.");
        }
        return record;
    }
}
