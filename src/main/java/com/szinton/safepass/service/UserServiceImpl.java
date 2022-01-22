package com.szinton.safepass.service;

import com.szinton.safepass.domain.User;
import com.szinton.safepass.dto.MasterPasswordDto;
import com.szinton.safepass.dto.UserDto;
import com.szinton.safepass.exception.ResourceNotFoundException;
import com.szinton.safepass.repo.UserRepository;
import com.szinton.safepass.security.PasswordEncryptionAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String MASTER_PASSWORD_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int MASTER_PASSWORD_LENGTH = 32;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordEncryptionAlgorithm encryptionAlgorithm;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User \"{}\" not found in the database.", username);
            throw new UsernameNotFoundException("User not found in the database.");
        } else {
            log.info("User \"{}\" found in the database.", username);
        }
        Collection<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("default"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public MasterPasswordDto registerUser(UserDto savedUser) {
        User emptyRecord = userRepository.findByUsername(savedUser.getUsername());
        if (emptyRecord != null) {
            throw new IllegalArgumentException("There is already a user with this username.");
        }

        String hashedPassword = passwordEncoder.encode(savedUser.getPassword());
        String masterPassword = generateMasterPassword();
        String encryptedUsername = encryptionAlgorithm.encrypt(savedUser.getUsername(), masterPassword);

        User user = new User(savedUser.getUsername(), encryptedUsername, hashedPassword);
        userRepository.save(user);
        log.info("Saving new user to the database.");
        return new MasterPasswordDto(masterPassword);
    }

    public User getUser(String username) {
        User record = userRepository.findByUsername(username);
        if (record == null) {
            throw new ResourceNotFoundException("User with the given username not found.");
        }
        return record;
    }

    private String generateMasterPassword() {
        SecureRandom rng = new SecureRandom();
        StringBuilder sb = new StringBuilder(MASTER_PASSWORD_LENGTH);
        for (int i = 0; i < MASTER_PASSWORD_LENGTH; i++) {
            int index = rng.nextInt(MASTER_PASSWORD_SYMBOLS.length());
            sb.append(MASTER_PASSWORD_SYMBOLS.charAt(index));
        }
        return sb.toString();
    }
}
