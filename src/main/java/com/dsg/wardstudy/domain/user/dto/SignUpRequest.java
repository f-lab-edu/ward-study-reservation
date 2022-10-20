package com.dsg.wardstudy.domain.user.dto;

import com.dsg.wardstudy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank
    @Length(min = 2, max = 12, message = "이름은 2글자 이상 12글자 이하여야 합니다.")
    private String name;

    @Email
    private String email;

    @NotBlank
    @Length(min = 2, max = 16, message = "닉네임은 2글자 이상 16글자 이하여야 합니다.")
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
