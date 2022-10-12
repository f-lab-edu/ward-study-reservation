package com.dsg.wardstudy.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    ENABLED("예약 가능"),
    CANCELED("예약 취소");

    private final String description;
}
