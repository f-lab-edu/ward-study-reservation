package com.dsg.wardstudy.controller.reservation;

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

    // 예약 등록
    @PostMapping("/study-group/{studyGroupId}/room/{roomId}/reservation")
    public ResponseEntity<ReservationDetail> create(
            @PathVariable("studyGroupId") Long studyGroupId,
            @PathVariable("roomId") String roomId,
            @RequestBody ReservationRequest reservationRequest) {
        log.info("reservation create");
        return new ResponseEntity<>(reservationService.create(
                reservationRequest, studyGroupId, roomId), HttpStatus.CREATED);
    }

    // 등록한 예약 상세 보기
    @GetMapping("/room/{roomId}/reservation/{reservationId}")
    public ResponseEntity<ReservationDetail> getByIds(
            @PathVariable("roomId") String roomId,
            @PathVariable("reservationId") String reservationId
    ) {
        log.info("reservation getById");
        return ResponseEntity.ok(reservationService.getByIds(roomId, reservationId));
    }

    // 해당 룸 예약 조회 startTime$endTime(option)
    @GetMapping("/room/{roomId}/reservation")
    public ResponseEntity<List<ReservationDetail>> getByRoomIdAndTime(
            @PathVariable("roomId") String roomId,
            @RequestParam(value = "startTime", required=false) String startTime,
            @RequestParam(value = "endTime", required=false) String endTime
    ) {
        log.info("reservation getByRoomId");
        return ResponseEntity.ok(byRoomIdAndTime(roomId, startTime, endTime));
    }

    private List<ReservationDetail> byRoomIdAndTime(String roomId, String startTime, String endTime) {

        if (startTime != null && endTime != null)  {
            return reservationService.getByRoomIdAndTime(roomId, startTime, endTime);
        } else {
            return reservationService.getByRoomId(roomId);
        }
    }

    // 해당 유저 예약  조회
    @GetMapping("/user/{userId}/reservation")
    public ResponseEntity<List<ReservationDetail>> getAllByUserId(@PathVariable("userId") Long userId) {
        log.info("reservation getAllByUserId");
        return ResponseEntity.ok(reservationService.getAllByUserId(userId));
    }

    // 예약 수정
    @PutMapping("/room/{roomId}/reservation/{reservationId}")
    public String updateById(
            @PathVariable("roomId") String roomId,
            @PathVariable("reservationId") String reservationId,
            @RequestBody ReservationUpdateRequest reservationRequest){
        log.info("reservation updateById");
        return reservationService.updateById(roomId, reservationId, reservationRequest);
    }

    // 예약 삭제
    @DeleteMapping("/reservation/{reservationId}")
    public ResponseEntity<String> deleteById(@PathVariable("reservationId") String reservationId) {
        log.info("reservation deleteById");
        reservationService.deleteById(reservationId);
        return new ResponseEntity<>("a reservation successfully deleted!", HttpStatus.OK);
    }


}
