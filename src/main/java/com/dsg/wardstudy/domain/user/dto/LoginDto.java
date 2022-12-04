package com.dsg.wardstudy.domain.user.dto;

import com.dsg.wardstudy.domain.user.entity.User;
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

    public static LoginDto mapToDto(User user) {
        return LoginDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }
}
