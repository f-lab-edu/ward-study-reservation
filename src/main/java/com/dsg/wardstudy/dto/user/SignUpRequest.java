package com.dsg.wardstudy.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {

    private String name;
    private String email;
    private String nickname;
    private String password;

    @Builder
    public SignUpRequest(String name, String email, String nickname, String password) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
