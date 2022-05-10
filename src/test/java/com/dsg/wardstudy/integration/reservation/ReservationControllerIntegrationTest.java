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
    @DisplayName("예약 등록")
    void create() throws Exception {

        // LocalDateTime -> String 으로 변환
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        createRequest = ReservationCreateRequest.builder()
                .userId(user.getId())
                .startTime(sTime)
                .endTime(eTime)
                .build();

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
    void getByIds() throws Exception {

        // LocalDateTime -> String 으로 변환
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Reservation savedReservation = reservationRepository.save(reservation);

        mockMvc.perform(get("/room/{roomId}/reservation/{reservationId}", room.getId(), savedReservation.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime", is(sTime)))
                .andExpect(jsonPath("$.endTime", is(eTime)));
    }


    @Test
    @DisplayName("해당 룸 예약 조회 startTime & endTime(o)")
    void getByRoomIdAndTimePeriod() throws Exception {

        LongStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .id(i + "||" + "2019-11-03 06:30:00")
                    .user(user)
                    .studyGroup(studyGroup)
                    .build();
            reservationRepository.save(reservation);
        });

        // LocalDateTime -> String 으로 변환
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


        mockMvc.perform(get("/room/{roomId}/reservation/query", room.getId())
                        .param("startTime", sTime)
                        .param("endTime", eTime))
                .andDo(print())
                .andExpect(status().isOk());


    }


    @Test
    @DisplayName("해당 룸 예약 조회")
    void getByRoomId() throws Exception {

        IntStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .id(i + "||" + "2019-11-03 06:30:00")
                    .user(user)
                    .studyGroup(studyGroup)
                    .room(room)
                    .build();
            reservationRepository.save(reservation);
        });

        mockMvc.perform(get("/room/{roomId}/reservation", room.getId()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("해당 룸 예약 조회 404에러")
    public void getByRoomId_ThrowException() throws Exception {

        Long roomId = 100L;
        reservationRepository.save(reservation);

        mockMvc.perform(get("/room/{roomId}/reservation", roomId))
                .andDo(print())
                .andExpect(status().isNotFound());

    }


    @Test
    @DisplayName("해당 유저 예약 조회")
    void getAllByUserId() throws Exception {

        IntStream.rangeClosed(1, 5).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .id(i + "||" + "2019-11-03 06:30:00")
                    .user(user)
                    .studyGroup(studyGroup)
                    .room(room)
                    .build();
            reservationRepository.save(reservation);
        });

        mockMvc.perform(get("/user/{userId}/reservation", user.getId()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("예약 수정")
    void updateById() throws Exception {

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

        mockMvc.perform(put("/room/{roomId}/reservation/{reservationId}", room.getId(), reservation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @DisplayName("예약 삭제")
    void deleteById() throws Exception {
        Reservation savedReservation = reservationRepository.save(reservation);

        mockMvc.perform(delete("/reservation/{reservationId}", savedReservation.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}