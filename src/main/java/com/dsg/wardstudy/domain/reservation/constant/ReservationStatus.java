package com.dsg.wardstudy.domain.reservation.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationStatus {

    ENABLED("예약 가능"),
    CANCELED("예약 취소");

    private final String description;
}
