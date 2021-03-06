package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.reservation.ReservationCreateRequest;
import com.dsg.wardstudy.dto.reservation.ReservationDetails;
import com.dsg.wardstudy.dto.reservation.ReservationUpdateRequest;
import com.dsg.wardstudy.exception.WSApiException;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import com.dsg.wardstudy.utils.TimeParsingUtils;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

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

    @Mock
    private TimeParsingUtils timeParsingUtils;

    @InjectMocks
    private ReservationServiceImpl reservationService;

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
    void givenReservation_whenSave_thenReturnReservationDetails() {
        // given - precondition or setup
        // LocalDateTime -> String ?????? ??????
        String sTime = timeParsingUtils.formatterString(reservation.getStartTime());
        String eTime = timeParsingUtils.formatterString(reservation.getEndTime());

        createRequest = ReservationCreateRequest.builder()
                .userId(user.getId())
                .startTime(sTime)
                .endTime(eTime)
                .build();

        // validate
        given(userGroupRepository.findUserTypeByUserIdAndSGId(createRequest.getUserId(), studyGroup.getId()))
                .willReturn(Optional.of(userGroup.getUserType()));

        // create ??????
        given(userRepository.findById(createRequest.getUserId())).willReturn(Optional.of(user));

        given(studyGroupRepository.findById(studyGroup.getId())).willReturn(Optional.of(studyGroup));

        given(roomRepository.findById(room.getId())).willReturn(Optional.of(room));

        given(reservationRepository.save(any(Reservation.class)))
                .willReturn(reservation);

        // when - action or the behaviour that we are going test
        ReservationDetails details = reservationService.create(studyGroup.getId(), room.getId(), createRequest);
        log.info("details: {}", details);

        // then - verify the output
        assertThat(details).isNotNull();
        assertThat(details.getStartTime()).isEqualTo(reservation.getStartTime());
        assertThat(details.getEndTime()).isEqualTo(reservation.getEndTime());
    }

    @Test
    public void givenRoomIdAndReservationId_whenGetById_thenReturnThrowException() {
        // getById_ThrowsException
        // given - precondition or setup
        String reservationId = "1||2020-11-03 06:30:00";

        Mockito.lenient().when(reservationRepository.findByRoomId(room.getId()))
                .thenReturn(List.of(reservation));
        Mockito.lenient().when(reservationRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        assertThatThrownBy(() -> {
            reservationService.getByRoomIdAndReservationId(room.getId(), reservationId);
        }).isInstanceOf(WSApiException.class);

        // then - verify the output
        verify(reservationRepository, never()).findByRoomId(anyLong());
        verify(reservationRepository, never()).findById(anyString());

    }

    // ?????? ??????
    @Test
    void givenUserId_whenGetAllById_thenReturnReservationDetailsList() {
        // getAllByUserId
        // given - precondition or setup
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        when(userGroupRepository.findSgIdsByUserId(user.getId()))
                .thenReturn(List.of(studyGroup.getId()));

        when(reservationRepository.findByStudyGroupIds(List.of(studyGroup.getId())))
                .thenReturn(List.of(reservation));

        // when - action or the behaviour that we are going test
        List<ReservationDetails> detailsList = reservationService.getAllByUserId(user.getId());
        log.info("detailsList: {}", detailsList);

        // then - verify the output
        assertThat(detailsList).isNotNull();
        assertThat(detailsList.size()).isEqualTo(1);

    }

    @Test
    void givenRoomIdAndTimePeriod_whenGetAllById_thenReturnReservationDetailsList() {
        // getByRoomIdAndTimePeriod
        // given - precondition or setup
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

        String sTime = timeParsingUtils.formatterString(startTime);
        String eTime = timeParsingUtils.formatterString(endTime);

        // parsing ?????? ?????? String -> LocalDateTime
        LocalDateTime parsingSTime = timeParsingUtils.formatterLocalDateTime(sTime);
        LocalDateTime parsingETime = timeParsingUtils.formatterLocalDateTime(eTime);

        given(reservationRepository.findByRoomIdAndTimePeriod(room.getId(), parsingSTime, parsingETime))
                .willReturn(List.of(reservation, reservation1));
        // when - action or the behaviour that we are going test
        List<ReservationDetails> detailsList = reservationService.getByRoomIdAndTimePeriod(room.getId(), sTime, eTime);
        log.info("detailsList: {}", detailsList);

        // then - verify the output
        assertThat(detailsList).isNotNull();
        assertThat(detailsList.size()).isEqualTo(2);
    }

    @Test
    void givenRoomId_whenGetAllById_thenReturnReservationDetailsList() {
        // getByRoomId
        // given - precondition or setup
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

        // when - action or the behaviour that we are going test
        List<ReservationDetails> detailsList = reservationService.getByRoomId(room.getId());
        log.info("detailsList: {}", detailsList);

        // then - verify the output
        assertThat(detailsList).isNotNull();
        assertThat(detailsList.size()).isEqualTo(2);

    }

    @Test
    void givenRoomIdAndReservationId_whenGetAllById_thenReturnReservationDetails() {
        // getByRoomIdAndReservationId
        // given - precondition or setup
        when(reservationRepository.findByRoomIdAndId(room.getId(), reservation.getId()))
                .thenReturn(Optional.of(reservation));

        // when - action or the behaviour that we are going test
        ReservationDetails details = reservationService.getByRoomIdAndReservationId(room.getId(), reservation.getId());
        log.info("details: {}", details);

        // then - verify the output
        assertThat(details).isNotNull();
        assertThat(details.getId()).isEqualTo(reservation.getId());
        assertThat(details.getStartTime()).isEqualTo(reservation.getStartTime());
        assertThat(details.getEndTime()).isEqualTo(reservation.getEndTime());
    }

    @Test
    void givenReservationUpdateRequest_whenUpdate_thenReturnUpdatedReservationId() {
        // given - precondition or setup
        String sTime = timeParsingUtils.formatterString(LocalDateTime.of(2022, Month.NOVEMBER, 3, 6, 30));
        String eTime = timeParsingUtils.formatterString(LocalDateTime.of(2022, Month.NOVEMBER, 3, 7, 30));
        
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

        // update ?????? ?????? : find -> new save -> old(reservation) delete
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
        // old reservation ??????
        willDoNothing().given(reservationRepository).delete(reservation);

        // when - action or the behaviour that we are going test
        String updateById = reservationService.updateById(room.getId(), reservation.getId(), updateRequest);
        log.info("updateById: {}", updateById);

        // then - verify the output
        assertThat(updateById).isNotNull();
        assertThat(updateById).isEqualTo(newReservation.getId());
    }

    @Test
    void givenReservationId_whenDelete_thenNothing() {
        // given - precondition or setup
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.of(reservation));

        // when - action or the behaviour that we are going test
        reservationService.deleteById(user.getId(), reservation.getId());

        // then - verify the output
        verify(reservationRepository).delete(reservation);

    }
}