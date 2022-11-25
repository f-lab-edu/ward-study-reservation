package com.dsg.wardstudy.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpResponse {

    private Long id;
    private String name;
    private String nickName;

    private String email;
    private String password;

    @Builder
    public SignUpResponse(Long id, String name, String nickName, String email, String password) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public static SignUpResponse mapToDto(UserInfo userInfo) {
        return SignUpResponse.builder()
                .id(userInfo.getId())
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .nickName(userInfo.getNickName())
                .password(userInfo.getPassword())
                .build();
    }
}
