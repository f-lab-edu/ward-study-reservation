package com.dsg.wardstudy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {

    PARTICIPANT("참여자"),
    LEADER("리더")
    ;

    private final String desceiption;
}
