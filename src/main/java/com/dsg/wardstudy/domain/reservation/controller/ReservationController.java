package com.dsg.wardstudy.domain.reservation.controller;

import com.dsg.wardstudy.common.auth.AuthUser;
import com.dsg.wardstudy.domain.reservation.dto.ReservationCommand;
import com.dsg.wardstudy.domain.reservation.dto.ReservationDetails;
import com.dsg.wardstudy.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 리더일때만 예약 등록, 수정 가능
    // 예약 등록
    @PostMapping("/study-group/{studyGroupId}/room/{roomId}/reservation")
    public ResponseEntity<ReservationDetails> register(
            @PathVariable("studyGroupId") Long studyGroupId,
            @PathVariable("roomId") Long roomId,
            @RequestBody ReservationCommand.RegisterReservation registerReservation,
            @AuthUser Long userId
    ) throws Exception {
        log.info("reservation register, studyGroupId: {}, roomId: {}, request: {}",
                studyGroupId,
                roomId,
                registerReservation);
        log.info("reservation register, userId: {}", userId);
        registerReservation.setUserId(userId);

        return new ResponseEntity<>(reservationService.register(
                studyGroupId, roomId, registerReservation), HttpStatus.CREATED);
    }

    // 등록한 예약 상세 보기
    @GetMapping("/room/{roomId}/reservation/{reservationToken}")
    public ResponseEntity<ReservationDetails> getReservation(
            @PathVariable("roomId") Long roomId,
            @PathVariable("reservationToken") String reservationToken,
            @AuthUser Long userId
    ) {
        log.info("reservation getById, roomId: {}, reservationToken: {}", roomId, reservationToken);
        log.info("reservation getByIds, userId: {}", userId);
        return ResponseEntity.ok(reservationService.getByRoomIdAndReservationToken(roomId, reservationToken));
    }

    // 해당 룸 예약 조회 startTime & endTime(mandatory)
    @GetMapping("/room/{roomId}/reservation/query")
    public ResponseEntity<List<ReservationDetails>> getByRoomIdAndTimePeriod(
            @PathVariable("roomId") Long roomId,
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime,
            @AuthUser Long userId
    ) {
        log.info("reservation getByRoomIdAndTime, roomId: {}, startTime: {}, endTime: {}", roomId, startTime, endTime);
        log.info("reservation getByRoomIdAndTimePeriod, userId: {}", userId);
        return ResponseEntity.ok(reservationService.getByRoomIdAndTimePeriod(roomId, startTime, endTime));
    }

    // 해당 룸 예약 조회 startTime & endTime x
    @GetMapping("/room/{roomId}/reservation")
    public ResponseEntity<List<ReservationDetails>> getByRoomId(
            @PathVariable("roomId") Long roomId,
            @AuthUser Long userId

    ) {
        log.info("reservation getByRoomId, roomId: {}", roomId);
        log.info("reservation getByRoomId, userId: {}", userId);
        return ResponseEntity.ok(reservationService.getByRoomId(roomId));
    }


    // 해당 유저 예약 조회
    @GetMapping("/reservation")
    public ResponseEntity<List<ReservationDetails>> getAllByUserId(
            @AuthUser Long userId
    ) {
        log.info("reservation getAllByUserId, userId: {}", userId);
        return ResponseEntity.ok(reservationService.getAllByUserId(userId));
    }

    // 예약 수정
    @PutMapping("/room/{roomId}/reservation/{reservationToken}")
    public String updateReservation(
            @PathVariable("roomId") Long roomId,
            @PathVariable("reservationToken") String reservationToken,
            @RequestBody ReservationCommand.UpdateReservation updateReservation,
            @AuthUser Long userId

    ) {
        log.info("reservation updateById, roomId: {}, reservationToken: {}", roomId, reservationToken);
        log.info("reservation updateById, userId: {}", userId);
        updateReservation.setUserId(userId);
        return reservationService.updateByToken(roomId, reservationToken, updateReservation);
    }

    // 예약 삭제
    @DeleteMapping("/reservation/{reservationToken}")
    public ResponseEntity<String> deleteReservation(
            @PathVariable("reservationToken") String reservationToken,
            @AuthUser Long userId
    ) {
        log.info("reservation deleteById, reservationToken: {}" ,reservationToken);
        log.info("reservation deleteById, userId: {}", userId);
        reservationService.deleteByToken(userId, reservationToken);
        return new ResponseEntity<>("a reservation successfully deleted!", HttpStatus.OK);
    }


}
