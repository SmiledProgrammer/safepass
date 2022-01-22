package com.szinton.safepass.dto;

import com.szinton.safepass.validation.SafeCharactersOnly;
import com.szinton.safepass.validation.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 6, max = 32, message = "Username must contain between 6 and 32 characters.")
    @SafeCharactersOnly(message = "Illegal characters used in the username. Only letters, digits, dots and dashes are allowed.")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    @StrongPassword
    private String password;
}
