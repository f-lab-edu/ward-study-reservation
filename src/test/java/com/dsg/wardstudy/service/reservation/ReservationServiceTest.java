package com.dsg.wardstudy.service.reservation;

import com.dsg.wardstudy.common.utils.TimeParsingUtils;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.reservation.service.ReservationServiceImpl;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.domain.reservation.dto.ReservationCommand;
import com.dsg.wardstudy.domain.reservation.dto.ReservationDetails;
import com.dsg.wardstudy.common.exception.WSApiException;
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


    @InjectMocks
    private ReservationServiceImpl reservationService;

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
                .name("dsg_person")
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
                .id(room.getId() +"||" + LocalDateTime.of(2019, Month.NOVEMBER, 3, 6, 30))
                .user(user)
                .studyGroup(studyGroup)
                .room(room)
                .startTime(LocalDateTime.of(2019, Month.NOVEMBER, 3, 6, 30))
                .endTime(LocalDateTime.of(2019, Month.NOVEMBER, 3, 7, 30))
                .build();
    }

    // TODO: NPE
    @Test
    void givenReservation_whenSave_thenReturnReservationDetails() {
        // given - precondition or setup
        // LocalDateTime -> String 으로 변환
        String sTime = TimeParsingUtils.formatterString(reservation.getStartTime());
        String eTime = TimeParsingUtils.formatterString(reservation.getEndTime());

        createRequest = ReservationCommand.RegisterReservation.builder()
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

        // when - action or the behaviour that we are going test
        ReservationDetails details = reservationService.register(studyGroup.getId(), room.getId(), createRequest);
        log.info("details: {}", details);

        // then - verify the output
        assertThat(details).isNotNull();
        assertThat(details.getStartTime()).isEqualTo(TimeParsingUtils.formatterString(reservation.getStartTime()));
        assertThat(details.getEndTime()).isEqualTo(TimeParsingUtils.formatterString(reservation.getEndTime()));
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

    // TODO: NPE
    @Test
    void givenRoomIdAndTimePeriod_whenGetAllById_thenReturnReservationDetailsList() {
        // getByRoomIdAndTimePeriod
        // given - precondition or setup
        LocalDateTime sTime = LocalDateTime.of(2019, Month.OCTOBER, 3, 5, 30);
        LocalDateTime eTime = LocalDateTime.of(2019, Month.OCTOBER, 3, 9, 30);

        Reservation reservation1 = Reservation.builder()
                .id(room.getId() + "||" + TimeParsingUtils.formatterString(sTime))
                .room(room)
                .user(user)
                .startTime(LocalDateTime.of(2019, Month.OCTOBER, 3, 5, 30))
                .endTime(LocalDateTime.of(2019, Month.OCTOBER, 3, 9, 30))
                .build();

        given(roomRepository.findById(room.getId()))
                .willReturn(Optional.of(room));

        String startTime = TimeParsingUtils.formatterString(sTime);
        String endTime = TimeParsingUtils.formatterString(eTime);

        // parsing 작업 추가 String -> LocalDateTime
        LocalDateTime parsingSTime = TimeParsingUtils.formatterLocalDateTime(startTime);
        LocalDateTime parsingETime = TimeParsingUtils.formatterLocalDateTime(endTime);

        given(reservationRepository.findByRoomIdAndTimePeriod(room.getId(), parsingSTime, parsingETime))
                .willReturn(List.of(reservation, reservation1));
        // when - action or the behaviour that we are going test
        List<ReservationDetails> detailsList = reservationService.getByRoomIdAndTimePeriod(room.getId(), startTime, endTime);
        log.info("detailsList: {}", detailsList);

        // then - verify the output
        assertThat(detailsList).isNotNull();
        assertThat(detailsList.size()).isEqualTo(1);
    }

    // TODO: NPE
    @Test
    void givenRoomId_whenGetAllById_thenReturnReservationDetailsList() {
        // getByRoomId
        // given - precondition or setup

        LocalDateTime sTime = LocalDateTime.of(2019, Month.OCTOBER, 3, 5, 30);
        LocalDateTime eTime = LocalDateTime.of(2019, Month.OCTOBER, 3, 9, 30);

        Reservation reservation1 = Reservation.builder()
                .id(room.getId() + "||" + TimeParsingUtils.formatterString(sTime))
                .room(room)
                .user(user)
                .startTime(LocalDateTime.of(2019, Month.OCTOBER, 3, 5, 30))
                .endTime(LocalDateTime.of(2019, Month.OCTOBER, 3, 9, 30))
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
        assertThat(details.getStartTime()).isEqualTo(TimeParsingUtils.formatterString(reservation.getStartTime()));
        assertThat(details.getEndTime()).isEqualTo(TimeParsingUtils.formatterString(reservation.getEndTime()));
    }

    @Test
    void givenReservationUpdateRequest_whenUpdate_thenReturnUpdatedReservationId() {
        // given - precondition or setup
        String sTime = TimeParsingUtils.formatterString(LocalDateTime.of(2022, Month.NOVEMBER, 3, 6, 30));
        String eTime = TimeParsingUtils.formatterString(LocalDateTime.of(2022, Month.NOVEMBER, 3, 7, 30));
        
        updateRequest = ReservationCommand.UpdateReservation.builder()
                .userId(user.getId())
                .studyGroupId(studyGroup.getId())
                .startTime(sTime)
                .endTime(eTime)
                .build();

        // validate
        given(studyGroupRepository.findById(updateRequest.getStudyGroupId()))
                .willReturn(Optional.of(studyGroup));
        given(userGroupRepository.findUserTypeByUserIdAndSGId(updateRequest.getUserId(), studyGroup.getId()))
                .willReturn(Optional.of(UserType.LEADER));

        // update 로직 변경 : find -> new save -> old(reservation) delete
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(reservationRepository.findById(reservation.getId())).willReturn(Optional.of(reservation));
        given(roomRepository.findById(room.getId())).willReturn(Optional.of(room));

        Reservation newReservation = Reservation.builder()
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
