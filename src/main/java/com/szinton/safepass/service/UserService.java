package com.szinton.safepass.service;

import com.szinton.safepass.domain.User;

public interface UserService {

    void saveUser(User user);
    User getUser(String username);

}
