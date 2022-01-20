package com.szinton.safepass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank
    @Size(min = 8, max = 32)
    private String username;
    @NotBlank
    @Size(min = 12, max = 128) // TODO: enhance password validation
    private String password;
}
