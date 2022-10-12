package com.dsg.wardstudy.controller.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.reservation.ReservationCommand;
import com.dsg.wardstudy.dto.reservation.ReservationDetails;
import com.dsg.wardstudy.exception.ErrorCode;
import com.dsg.wardstudy.exception.WSApiException;
import com.dsg.wardstudy.service.reservation.ReservationService;
import com.dsg.wardstudy.type.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation reservation;
    private User user;
    private UserGroup userGroup;
    private StudyGroup studyGroup;
    private Room room;

    private ReservationCommand.RegisterReservation createRequest;
    private ReservationCommand.UpdateReservation updateRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .build();

        userGroup = UserGroup.builder()
                .id(1L)
                .user(user)
                .userType(UserType.L)
                .studyGroup(studyGroup)
                .build();

        studyGroup = StudyGroup.builder()
                .id(1L)
                .build();

        room = Room.builder()
                .id(1L)
                .build();

        reservation = Reservation.builder()
//                .id("1||2019-11-03 06:30:00")
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .startTime(LocalDateTime.of(2019, Month.NOVEMBER, 3, 6, 30))
                .endTime(LocalDateTime.of(2019, Month.NOVEMBER, 3, 7, 30))
                .build();
    }

    @Test
    @DisplayName("예약 등록")
    void givenReservationCreateRequestAndSGIdAndRoomId_whenCreate_thenReturnReservationDetails() throws Exception {
        // given - precondition or setup
        // LocalDateTime -> String 으로 변환
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        createRequest = ReservationCommand.RegisterReservation.builder()
                .userId(user.getId())
                .startTime(sTime)
                .endTime(eTime)
                .build();

        ReservationDetails reservationDetails = ReservationDetails.builder()
                .startTime(sTime)
                .endTime(eTime)
                .registerId(user.getId())
                .studyGroupId(studyGroup.getId())
                .roomId(room.getId())
                .build();

        given(reservationService.register(studyGroup.getId(), room.getId(), createRequest))
                .willReturn(reservationDetails);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(post("/study-group/{studyGroupId}/room/{roomId}/reservation", studyGroup.getId(), room.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startTime", is(sTime)))
                .andExpect(jsonPath("$.endTime", is(eTime)));

    }


    @Test
    @DisplayName("등록한 예약 상세 보기")
    void givenRoomIdAndReservationId_whenGet_thenReturnReservationDetails() throws Exception {
        // getByIds
        // given - precondition or setup
        // LocalDateTime -> String 으로 변환
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        ReservationDetails reservationDetails = ReservationDetails.builder()
                .startTime(sTime)
                .endTime(eTime)
                .registerId(user.getId())
                .studyGroupId(studyGroup.getId())
                .roomId(room.getId())
                .build();
        given(reservationService.getByRoomIdAndReservationId(room.getId(), reservation.getId()))
                .willReturn(reservationDetails);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation/{reservationId}", room.getId(), reservation.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime", is(sTime)))
                .andExpect(jsonPath("$.endTime", is(eTime)));
    }


    @Test
    @DisplayName("해당 룸 예약 조회 startTime & endTime(o)")
    void givenRoomIdAndTimePeriod_whenGet_thenReturnReservationDetailsList() throws Exception {
        // getByRoomIdAndTimePeriod
        // given - precondition or setup
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<ReservationDetails> detailsList = new ArrayList<>();
        ReservationDetails reservationDetails = ReservationDetails.builder()
                .startTime(sTime)
                .endTime(eTime)
                .registerId(user.getId())
                .studyGroupId(studyGroup.getId())
                .roomId(room.getId())
                .build();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            detailsList.add(reservationDetails);
        });

        given(reservationService.getByRoomIdAndTimePeriod(room.getId(), sTime, eTime))
                .willReturn(detailsList);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation/query", room.getId())
                        .param("startTime", sTime)
                        .param("endTime", eTime))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$.[0].startTime", is(sTime)))
                .andExpect(jsonPath("$.[0].endTime", is(eTime)));


    }


    @Test
    @DisplayName("해당 룸 예약 조회")
    void givenRoomId_whenGet_thenReturnReservationDetailsList() throws Exception {
        // getByRoomId
        // given - precondition or setup
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<ReservationDetails> detailsList = new ArrayList<>();
        ReservationDetails reservationDetails = ReservationDetails.builder()
                .startTime(sTime)
                .endTime(eTime)
                .registerId(user.getId())
                .studyGroupId(studyGroup.getId())
                .roomId(room.getId())
                .build();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            detailsList.add(reservationDetails);
        });


        given(reservationService.getByRoomId(room.getId()))
                .willReturn(detailsList);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation", room.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$.[0].startTime", is(sTime)))
                .andExpect(jsonPath("$.[0].endTime", is(eTime)));

    }

    @Test
    @DisplayName("해당 룸 예약 조회 404에러")
    public void givenInvalidRoomId_whenGet_thenReturn404() throws Exception {
        // given - precondition or setup
        Long roomId = 100L;
        given(reservationService.getByRoomId(roomId))
                .willThrow(new WSApiException(ErrorCode.NO_FOUND_ENTITY,
                        "can't find a room by " + "roomId: " +  roomId));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation", roomId))
                .andDo(print())
                .andExpect(status().isNotFound());

    }


    @Test
    @DisplayName("해당 유저 예약  조회")
    void givenUserId_whenGet_thenReturnReservationDetailsList() throws Exception {
        // getAllByUserId
        // given - precondition or setup
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<ReservationDetails> detailsList = new ArrayList<>();
        ReservationDetails reservationDetails = ReservationDetails.builder()
                .startTime(sTime)
                .endTime(eTime)
                .registerId(user.getId())
                .studyGroupId(studyGroup.getId())
                .roomId(room.getId())
                .build();

        IntStream.rangeClosed(1, 5).forEach(i -> {
            detailsList.add(reservationDetails);
        });

        given(reservationService.getAllByUserId(user.getId()))
                .willReturn(detailsList);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/users/{userId}/reservation", user.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.length()", is(5)))
                .andExpect(jsonPath("$.[0].startTime", is(sTime)))
                .andExpect(jsonPath("$.[0].endTime", is(eTime)));

    }

    @Test
    @DisplayName("예약 수정")
    void givenReservationUpdateRequest_whenUpdate_thenReturnUpdatedReservationId() throws Exception {
        // given - precondition or setup
        String updateSTime = LocalDateTime.of(2022, Month.NOVEMBER, 3, 6, 30)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String updateETime = LocalDateTime.of(2022, Month.NOVEMBER, 3, 6, 30)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        updateRequest = ReservationUpdateRequest.builder()
                .userId(user.getId())
                .studyGroupId(studyGroup.getId())
                .startTime(updateSTime)
                .endTime(updateETime)
                .build();

        given(reservationService.updateById(room.getId(), reservation.getId(), updateRequest))
                .willReturn(reservation.getId());

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(put("/room/{roomId}/reservation/{reservationId}", room.getId(), reservation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("예약 삭제")
    void givenReservationId_whenDelete_thenReturn200() throws Exception {
        // given - precondition or setup
        willDoNothing().given(reservationService).deleteById(user.getId(), reservation.getId());

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(delete("/users/{userId}/reservation/{reservationId}",user.getId(), reservation.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
