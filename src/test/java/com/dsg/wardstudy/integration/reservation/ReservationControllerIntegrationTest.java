package com.dsg.wardstudy.integration.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.reservation.ReservationCreateRequest;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.type.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation reservation;
    private User user;
    private UserGroup userGroup;
    private StudyGroup studyGroup;
    private Room room;

    private ReservationCreateRequest createRequest;
    private ReservationUpdateRequest updateRequest;

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
                .id("1||2019-11-03 06:30:00")
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .startTime(LocalDateTime.of(2019, Month.NOVEMBER, 3, 6, 30))
                .endTime(LocalDateTime.of(2019, Month.NOVEMBER, 3, 7, 30))
                .build();
    }

    @Test
    @DisplayName("?????? ??????")
    void givenReservationCreateRequestAndSGIdAndRoomId_whenCreate_thenReturnReservationDetails() throws Exception {
        // given - precondition or setup
        // LocalDateTime -> String ?????? ??????
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        createRequest = ReservationCreateRequest.builder()
                .userId(user.getId())
                .startTime(sTime)
                .endTime(eTime)
                .build();

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(post("/study-group/{studyGroupId}/room/{roomId}/reservation", studyGroup.getId(), room.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.startTime", is(sTime)))
                .andExpect(jsonPath("$.endTime", is(eTime)));

    }


    @Test
    @DisplayName("????????? ?????? ?????? ??????")
    void givenRoomIdAndReservationId_whenGet_thenReturnReservationDetails() throws Exception {
        // given - precondition or setup
        // LocalDateTime -> String ?????? ??????
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Reservation savedReservation = reservationRepository.save(reservation);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation/{reservationId}", room.getId(), savedReservation.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime", is(sTime)))
                .andExpect(jsonPath("$.endTime", is(eTime)));
    }


    @Test
    @DisplayName("?????? ??? ?????? ?????? startTime & endTime(o)")
    void givenRoomIdAndTimePeriod_whenGet_thenReturnReservationDetailsList() throws Exception {
        // given - precondition or setup
        LongStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .id(i + "||" + "2019-11-03 06:30:00")
                    .user(user)
                    .studyGroup(studyGroup)
                    .build();
            reservationRepository.save(reservation);
        });

        // LocalDateTime -> String ?????? ??????
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation/query", room.getId())
                        .param("startTime", sTime)
                        .param("endTime", eTime))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("?????? ??? ?????? ??????")
    void givenRoomId_whenGet_thenReturnReservationDetailsList() throws Exception {
        // given - precondition or setup
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .id(i + "||" + "2019-11-03 06:30:00")
                    .user(user)
                    .studyGroup(studyGroup)
                    .room(room)
                    .build();
            reservationRepository.save(reservation);
        });

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation", room.getId()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("?????? ??? ?????? ?????? 404??????")
    public void givenInvalidRoomId_whenGet_thenReturn404() throws Exception {
        // given - precondition or setup
        Long roomId = 100L;
        reservationRepository.save(reservation);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation", roomId))
                .andDo(print())
                .andExpect(status().isNotFound());

    }


    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void givenUserId_whenGet_thenReturnReservationDetailsList() throws Exception {
        // given - precondition or setup
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .id(i + "||" + "2019-11-03 06:30:00")
                    .user(user)
                    .studyGroup(studyGroup)
                    .room(room)
                    .build();
            reservationRepository.save(reservation);
        });
        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/users/{userId}/reservation", user.getId()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("?????? ??????")
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
        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(put("/room/{roomId}/reservation/{reservationId}", room.getId(), reservation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("?????? ??????")
    void givenReservationId_whenDelete_thenReturn200() throws Exception {
        // given - precondition or setup
        Reservation savedReservation = reservationRepository.save(reservation);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(delete("/reservation/{reservationId}", savedReservation.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}