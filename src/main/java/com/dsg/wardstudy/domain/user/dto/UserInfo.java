package com.dsg.wardstudy.domain.user.dto;

import com.dsg.wardstudy.domain.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfo {

    private Long id;
    private String name;
    private String nickName;

    private String email;
    private String password;

    @Builder
    public UserInfo(Long id, String name, String nickName, String email, String password) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public static UserInfo mapToDto(User savedUser) {
        return UserInfo.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .nickName(savedUser.getNickname())
                .password(savedUser.getPassword())
                .build();
    }
}
