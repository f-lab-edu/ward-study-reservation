package com.dsg.wardstudy.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class LoginDto {

    private Long id;
    private String name;

    @Email
    private String email;
    @NotBlank
    private String password;

    @Builder
    public LoginDto(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
