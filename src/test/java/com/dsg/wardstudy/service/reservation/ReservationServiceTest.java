package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.reservation.ReservationCreateRequest;
import com.dsg.wardstudy.dto.reservation.ReservationDetails;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;
import com.dsg.wardstudy.exception.ResourceNotFoundException;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserGroupRepository userGroupRepository;
    @Mock
    private StudyGroupRepository studyGroupRepository;
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReservationService reservationService;

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
                .name("dsg_person")
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
    void create() {
        // LocalDateTime -> String 으로 변환
        String sTime = reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = reservation.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        createRequest = ReservationCreateRequest.builder()
                .userId(user.getId())
                .startTime(sTime)
                .endTime(eTime)
                .build();

        // validate
        given(userGroupRepository.findUserTypeByUserIdAndSGId(createRequest.getUserId(), studyGroup.getId()))
                .willReturn(Optional.of(userGroup.getUserType()));

        // create 로직
        given(userRepository.findById(createRequest.getUserId())).willReturn(Optional.of(user));

        given(studyGroupRepository.findById(studyGroup.getId())).willReturn(Optional.of(studyGroup));

        given(roomRepository.findById(room.getId())).willReturn(Optional.of(room));

        given(reservationRepository.save(any(Reservation.class)))
                .willReturn(reservation);

        ReservationDetails details = reservationService.create(createRequest, studyGroup.getId(), room.getId());
        log.info("details: {}", details);

        assertThat(details).isNotNull();
        assertThat(details.getStartTime()).isEqualTo(reservation.getStartTime());
        assertThat(details.getEndTime()).isEqualTo(reservation.getEndTime());
    }

    @Test
    public void getById_ThrowsException() {

        String reservationId = "1||2020-11-03 06:30:00";

        Mockito.lenient().when(reservationRepository.findByRoomId(room.getId()))
                .thenReturn(List.of(reservation));
        Mockito.lenient().when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            reservationService.getByRoomIdAndReservationId(room.getId(), reservationId);
        }).isInstanceOf(ResourceNotFoundException.class);


    }

    // 해당 유저
    @Test
    void getAllByUserId() {

        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        when(userGroupRepository.findSgIdsByUserId(user.getId()))
                .thenReturn(List.of(studyGroup.getId()));

        when(reservationRepository.findByStudyGroupIds(List.of(studyGroup.getId())))
                .thenReturn(List.of(reservation));

        List<ReservationDetails> detailsList = reservationService.getAllByUserId(user.getId());
        log.info("detailsList: {}", detailsList);

        assertThat(detailsList).isNotNull();
        assertThat(detailsList.size()).isEqualTo(1);

    }

    @Test
    void getByRoomIdAndTimePeriod() {
        LocalDateTime startTime = LocalDateTime.of(2019, Month.OCTOBER, 3, 5, 30);

        LocalDateTime endTime = LocalDateTime.of(2019, Month.OCTOBER, 3, 9, 30);

        Reservation reservation1 = Reservation.builder()
                .id("1||2019-10-03 08:30:00")
                .room(room)
                .user(user)
                .startTime(LocalDateTime.of(2019, Month.OCTOBER, 3, 8, 30))
                .endTime(LocalDateTime.of(2019, Month.OCTOBER, 3, 9, 30))
                .build();

        given(roomRepository.findById(room.getId()))
                .willReturn(Optional.of(room));

        given(reservationRepository.findByRoomIdAndTimePeriod(room.getId(), startTime, endTime))
                .willReturn(List.of(reservation, reservation1));

        String sTime = startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<ReservationDetails> detailsList = reservationService.getByRoomIdAndTimePeriod(room.getId(), sTime, eTime);
        log.info("detailsList: {}", detailsList);

        assertThat(detailsList).isNotNull();
        assertThat(detailsList.size()).isEqualTo(2);
    }

    @Test
    void getByRoomId() {

        Reservation reservation1 = Reservation.builder()
                .id("1||2019-10-03 06:30:00")
                .room(room)
                .user(user)
                .startTime(LocalDateTime.of(2019, Month.OCTOBER, 3, 6, 30))
                .endTime(LocalDateTime.of(2019, Month.OCTOBER, 3, 7, 30))
                .build();
        given(roomRepository.findById(room.getId()))
                .willReturn(Optional.of(room));
        given(reservationRepository.findByRoomId(room.getId()))
                .willReturn(List.of(reservation, reservation1));

        List<ReservationDetails> detailsList = reservationService.getByRoomId(room.getId());
        log.info("detailsList: {}", detailsList);

        assertThat(detailsList).isNotNull();
        assertThat(detailsList.size()).isEqualTo(2);

    }

    @Test
    void getByRoomIdAndReservationId() {

        when(reservationRepository.findByRoomIdAndId(room.getId(), reservation.getId()))
                .thenReturn(Optional.of(reservation));

        ReservationDetails details = reservationService.getByRoomIdAndReservationId(room.getId(), reservation.getId());
        log.info("details: {}", details);

        assertThat(details).isNotNull();
        assertThat(details.getId()).isEqualTo(reservation.getId());
        assertThat(details.getStartTime()).isEqualTo(reservation.getStartTime());
        assertThat(details.getEndTime()).isEqualTo(reservation.getEndTime());
    }

    @Test
    void updateById() {

        String sTime = LocalDateTime.of(2022, Month.NOVEMBER, 3, 6, 30)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String eTime = LocalDateTime.of(2022, Month.NOVEMBER, 3, 7, 30)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        updateRequest = ReservationUpdateRequest.builder()
                .userId(user.getId())
                .studyGroupId(studyGroup.getId())
                .startTime(sTime)
                .endTime(eTime)
                .build();

        // validate
        given(studyGroupRepository.findById(updateRequest.getStudyGroupId()))
                .willReturn(Optional.of(studyGroup));
        given(userGroupRepository.findUserTypeByUserIdAndSGId(updateRequest.getUserId(), studyGroup.getId()))
                .willReturn(Optional.of(UserType.L));

        // update 로직 변경 : find -> new save -> old(reservation) delete
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(reservationRepository.findById(reservation.getId())).willReturn(Optional.of(reservation));
        given(roomRepository.findById(room.getId())).willReturn(Optional.of(room));

        Reservation newReservation = Reservation.builder()
                .id(room.getId() + "||" + updateRequest.getStartTime())
                .startTime(LocalDateTime.of(2022, Month.NOVEMBER, 3, 6, 30))
                .endTime(LocalDateTime.of(2022, Month.NOVEMBER, 3, 7, 30))
                .user(user)
                .studyGroup(reservation.getStudyGroup())
                .room(room)
                .build();

        given(reservationRepository.save(any(Reservation.class)))
                .willReturn(newReservation);
        // old reservation 제거
        willDoNothing().given(reservationRepository).delete(reservation);

        String updateById = reservationService.updateById(room.getId(), reservation.getId(), updateRequest);
        log.info("updateById: {}", updateById);

        assertThat(updateById).isNotNull();
        assertThat(updateById).isEqualTo(newReservation.getId());
    }

    @Test
    void deleteById() {

        given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.of(reservation));
        willDoNothing().given(reservationRepository).delete(reservation);

        reservationService.deleteById(reservation.getId());

        verify(reservationRepository).delete(reservation);

    }
}