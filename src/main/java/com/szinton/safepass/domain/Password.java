package com.szinton.safepass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "passwords")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Password {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String serviceName;

    @Column(length = 512, nullable = false)
    private String password;

    @Column(nullable = false)
    private Long userId;

    public Password(String serviceName, String password, Long userId) {
        this.serviceName = serviceName;
        this.password = password;
        this.userId = userId;
    }
}
