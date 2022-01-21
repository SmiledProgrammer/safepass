package com.szinton.safepass.service;

import com.szinton.safepass.domain.User;
import com.szinton.safepass.dto.MasterPasswordDto;
import com.szinton.safepass.dto.UserDto;

public interface UserService {

    MasterPasswordDto registerUser(UserDto user);

    User getUser(String username);
}
