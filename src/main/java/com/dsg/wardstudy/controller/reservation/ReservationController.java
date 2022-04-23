package com.dsg.wardstudy.controller.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.dto.reservation.ReservationDetail;
import com.dsg.wardstudy.dto.reservation.ReservationRequest;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;
import com.dsg.wardstudy.service.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

//    @PostMapping("/reservation")
//    public ResponseEntity<ReservationDetail> create(@RequestBody ReservationRequest reservationRequest) {
//        log.info("reservation create");
//        return new ResponseEntity<>(reservationService.create(reservationRequest), HttpStatus.CREATED);
//    }

    // 예약 등록
    @PostMapping("/study-group/{studyGroupId}/room/{roomId}/reservation")
    public ResponseEntity<ReservationDetail> create(
            @PathVariable("studyGroupId") Long studyGroupId,
            @PathVariable("roomId") Long roomId,
            @RequestBody ReservationRequest reservationRequest) {
        log.info("reservation create");
        return new ResponseEntity<>(reservationService.create(
                reservationRequest, studyGroupId, roomId), HttpStatus.CREATED);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<ReservationDetail> getById(@PathVariable("reservationId") Long reservationId) {
        log.info("reservation getById");
        return ResponseEntity.ok(reservationService.getById(reservationId));
    }

    // 해당 룸 예약 조회 startTime$endTime
    @GetMapping("/room/{roomId}/reservation")
    public ResponseEntity<List<ReservationDetail>> getByRoomIdAndTime(
            @PathVariable("roomId") Long roomId,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime
    ) {
        log.info("reservation getByRoomId");
        return ResponseEntity.ok(reservationService.getByRoomIdAndTime(roomId, startTime, endTime));
    }

//    @GetMapping("/reservation")
//    public ResponseEntity<List<ReservationDetail>> getAll() {
//        log.info("reservation getAll");
//        return ResponseEntity.ok(reservationService.getAll());
//    }

    // 해당 유저 예약  조회
    @GetMapping("/user/{userId}/reservation")
    public ResponseEntity<List<ReservationDetail>> getAllByUserId(@PathVariable("userId") Long userId) {
        log.info("reservation getAllByUserId");
        return ResponseEntity.ok(reservationService.getAllByUserId(userId));
    }

    @PutMapping("/reservation/{reservationId}")
    public Long updateById(@PathVariable("reservationId") Long reservationId,
                           @RequestBody ReservationUpdateRequest reservationRequest){
        log.info("reservation updateById");
        return reservationService.updateById(reservationId, reservationRequest);
    }

    @DeleteMapping("/reservation/{reservationId}")
    public ResponseEntity<String> deleteById(@PathVariable("reservationId") Long reservationId) {
        log.info("reservation deleteById");
        reservationService.deleteById(reservationId);
        return new ResponseEntity<>("a reservation successfully deleted!", HttpStatus.OK);
    }


}
