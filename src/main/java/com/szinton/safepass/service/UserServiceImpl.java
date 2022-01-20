package com.szinton.safepass.service;

import com.szinton.safepass.domain.User;
import com.szinton.safepass.dto.UserDto;
import com.szinton.safepass.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public void saveUser(UserDto savedUser) {
        log.info("Saving new user to the database.");
        String hashedPassword = passwordEncoder.encode(savedUser.getPassword());
        User user = new User(savedUser.getUsername(), hashedPassword);
        userRepository.save(user);
    }
}
