package com.szinton.safepass.repo;

import com.szinton.safepass.domain.Password;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {

    Password findByUserIdAndServiceName(Long userId, String serviceName);

    List<Password> findByUserId(Long userId);
}
