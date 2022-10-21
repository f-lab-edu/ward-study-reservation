package com.dsg.wardstudy.domain.user.dto;

import com.dsg.wardstudy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SignUpRequest {
    
    @NotBlank
    @Size(min = 2, max = 12, message = "이름은 2글자 이상 12글자 이하여야 합니다.")
    private String name;

    @NotBlank
    @Pattern(regexp = "[a-zA-z0-9]+@[a-zA-z]+[.]+[a-zA-z.]+", message= "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank
    @Size(min = 2, max = 16, message = "닉네임은 2글자 이상 16글자 이하여야 합니다.")
    private String nickname;

    @NotBlank
    private String password;

    @Builder
    public SignUpRequest(String name, String email, String nickname, String password) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public static User mapToEntity(SignUpRequest signUpDto) {
        return User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .password(signUpDto.getPassword())
                .build();
    }
}
