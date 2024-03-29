package com.dsg.wardstudy.domain.user.dto;

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
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{4,15}$",
            message= "비밀번호는 4글자 이상, 16글자 미만 그리고 영문/숫자/특수문자 포함이어야 합니다.")
    private String password;

    @Builder
    public SignUpRequest(String name, String email, String nickname, String password) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

}
