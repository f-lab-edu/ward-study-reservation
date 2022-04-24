package com.dsg.wardstudy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {

    P("Participant"),
    L("Leader")
    ;

    private final String desceiption;
}
