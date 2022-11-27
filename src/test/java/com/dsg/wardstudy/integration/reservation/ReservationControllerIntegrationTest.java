package com.dsg.wardstudy.integration.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.domain.reservation.dto.ReservationCommand;
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
                .userType(UserType.LEADER)
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

    // TODO: IntegrationControllerTest부터는 Config 파일과 직접적으로 연관되어 어플리케이션 로딩 실패
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
    @DisplayName("등록한 예약 상세 보기")
    void givenRoomIdAndReservationToken_whenGet_thenReturnReservationDetails() throws Exception {
        // given - precondition or setup
        // LocalDateTime -> String 으로 변환
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Reservation savedReservation = reservationRepository.save(reservation);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/room/{roomId}/reservation/{reservationToken}", room.getId(), savedReservation.getReservationToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime", is(sTime)))
                .andExpect(jsonPath("$.endTime", is(eTime)));
    }


    @Test
    @DisplayName("해당 룸 예약 조회 startTime & endTime(o)")
    void givenRoomIdAndTimePeriod_whenGet_thenReturnReservationDetailsList() throws Exception {
        // given - precondition or setup
        LongStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
//                    .id(i + "||" + "2019-11-03 06:30:00")
                    .user(user)
                    .studyGroup(studyGroup)
                    .build();
            reservationRepository.save(reservation);
        });

        // LocalDateTime -> String 으로 변환
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
    @DisplayName("해당 룸 예약 조회")
    void givenRoomId_whenGet_thenReturnReservationDetailsList() throws Exception {
        // given - precondition or setup
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
//                    .id(i + "||" + "2019-11-03 06:30:00")
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
    @DisplayName("해당 룸 예약 조회 404에러")
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
    @DisplayName("해당 유저 예약 조회")
    void givenUserId_whenGet_thenReturnReservationDetailsList() throws Exception {
        // given - precondition or setup
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
//                    .id(i + "||" + "2019-11-03 06:30:00")
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
    @DisplayName("예약 수정")
    void givenReservationUpdateRequest_whenUpdate_thenReturnUpdatedReservationToken() throws Exception {
        // given - precondition or setup
        String updateSTime = LocalDateTime.of(2022, Month.NOVEMBER, 3, 6, 30)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String updateETime = LocalDateTime.of(2022, Month.NOVEMBER, 3, 6, 30)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        updateRequest = ReservationCommand.UpdateReservation.builder()
                .userId(user.getId())
                .studyGroupId(studyGroup.getId())
                .startTime(updateSTime)
                .endTime(updateETime)
                .build();
        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(put("/room/{roomId}/reservation/{reservationToken}", room.getId(), reservation.getReservationToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("예약 삭제")
    void givenReservationToken_whenDelete_thenReturn200() throws Exception {
        // given - precondition or setup
        Reservation savedReservation = reservationRepository.save(reservation);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(delete("/reservation/{reservationToken}", savedReservation.getReservationToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
