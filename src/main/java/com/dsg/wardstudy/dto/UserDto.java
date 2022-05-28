package com.dsg.wardstudy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private String name;
    private String email;

    @Builder
    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
