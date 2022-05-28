package com.dsg.wardstudy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum Status {

    ENABLED("reservation enabled"),
    CANCELED("reservation canceled");

    private final String description;
}
