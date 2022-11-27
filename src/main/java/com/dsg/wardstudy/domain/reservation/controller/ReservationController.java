package com.dsg.wardstudy.domain.reservation.controller;

import com.dsg.wardstudy.config.auth.AuthUser;
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
    @GetMapping("/room/{roomId}/reservation/{reservationId}")
    public ResponseEntity<ReservationDetails> getByIds(
            @PathVariable("roomId") Long roomId,
            @PathVariable("reservationId") String reservationId
    ) {
        log.info("reservation getById, roomId: {}, reservationId: {}", roomId, reservationId);
        return ResponseEntity.ok(reservationService.getByRoomIdAndReservationId(roomId, reservationId));
    }

    // 해당 룸 예약 조회 startTime & endTime(mandatory)
    @GetMapping("/room/{roomId}/reservation/query")
    public ResponseEntity<List<ReservationDetails>> getByRoomIdAndTimePeriod(
            @PathVariable("roomId") Long roomId,
            @RequestParam(value = "startTime") String startTime,
            @RequestParam(value = "endTime") String endTime
    ) {
        log.info("reservation getByRoomIdAndTime, roomId: {}, startTime: {}, endTime: {}", roomId, startTime, endTime);
        return ResponseEntity.ok(reservationService.getByRoomIdAndTimePeriod(roomId, startTime, endTime));
    }

    // 해당 룸 예약 조회 startTime & endTime x
    @GetMapping("/room/{roomId}/reservation")
    public ResponseEntity<List<ReservationDetails>> getByRoomId(@PathVariable("roomId") Long roomId) {

        log.info("reservation getByRoomId, roomId: {}", roomId);
        return ResponseEntity.ok(reservationService.getByRoomId(roomId));
    }


    // 해당 유저 예약  조회
    @GetMapping("/users/{userId}/reservation")
    public ResponseEntity<List<ReservationDetails>> getAllByUserId(@PathVariable("userId") Long userId) {
        log.info("reservation getAllByUserId, userId: {}", userId);
        return ResponseEntity.ok(reservationService.getAllByUserId(userId));
    }

    // 예약 수정
    @PutMapping("/room/{roomId}/reservation/{reservationId}")
    public String updateById(
            @PathVariable("roomId") Long roomId,
            @PathVariable("reservationId") String reservationId,
            @RequestBody ReservationCommand.UpdateReservation updateReservation) {
        log.info("reservation updateById, roomId: {}, reservationId: {}", roomId, reservationId);
        return reservationService.updateById(roomId, reservationId, updateReservation);
    }

    // 예약 삭제
    @DeleteMapping("/users/{userId}/reservation/{reservationId}")
    public ResponseEntity<String> deleteById(
            @PathVariable("userId") Long userId,
            @PathVariable("reservationId") String reservationId) {
        log.info("reservation deleteById, userId: {}, reservationId: {}" ,userId, reservationId);
        reservationService.deleteById(userId, reservationId);
        return new ResponseEntity<>("a reservation successfully deleted!", HttpStatus.OK);
    }


}
