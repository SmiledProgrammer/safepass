package com.szinton.safepass.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 32, nullable = false)
    private String username;

    @Column(length = 512, nullable = false)
    private String encryptedUsername;

    @Column(length = 64, nullable = false)
    private String password;

    public User(String username, String encryptedUsername, String password) {
        this.username = username;
        this.encryptedUsername = encryptedUsername;
        this.password = password;
    }
}
