package com.dsg.wardstudy.domain.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserType {

    PARTICIPANT("스터디 참여자"),
    LEADER("스터디 리더, 등록자");

    private final String description;
}
