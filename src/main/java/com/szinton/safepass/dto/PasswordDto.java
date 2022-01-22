package com.szinton.safepass.dto;

import com.szinton.safepass.validation.SafeCharactersOnly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {
    @NotBlank(message = "Service name cannot be blank.")
    @Size(max = 255, message = "Service name too long.")
    @SafeCharactersOnly(message = "Illegal characters used in the service name. Only letters, digits, dots and dashes are allowed.")
    private String serviceName;

    @NotBlank(message = "Password cannot be blank.")
    @Size(max = 512, message = "Password too long.")
    private String password;
}
