package com.szinton.safepass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordDto {
    @NotBlank
    @Size(max = 255)
    private String serviceName;
    @NotBlank
    @Size(max = 512)
    private String password;
}
