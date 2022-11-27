package com.dsg.wardstudy.integration.reservation;

import com.dsg.wardstudy.common.utils.TimeParsingUtils;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationRepositoryIntegrationTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private RoomRepository roomRepository;

    private User user;
    private UserGroup userGroup;
    private StudyGroup studyGroup;
    private Room room;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("dsgfunk@gmail.com")
                .build();

        userGroup = UserGroup.builder()
                .userType(UserType.PARTICIPANT)
                .build();

        studyGroup = StudyGroup.builder()
                .title("title_")
                .content("study 번 방")
                .build();

        room = Room.builder()
                .name("roomA")
                .build();

    }

    @Test
    public void givenReservation_whenSave_thenReturnSavedReservation() {
        // given - precondition or setup
        String startTime = "2022-04-24 10:30:00";
        LocalDateTime sTime = TimeParsingUtils.formatterLocalDateTime(startTime);
        Reservation savedReservation = getReservation();
        log.info("savedReservation: {}", savedReservation);

        // then - verify the output
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getStartTime()).isEqualTo(sTime);
        assertThat(savedReservation.getUser().getEmail()).isEqualTo(this.user.getEmail());
        assertThat(savedReservation.getStudyGroup().getTitle()).isEqualTo(this.studyGroup.getTitle());
        assertThat(savedReservation.getRoom().getId()).isEqualTo(this.room.getId());
        assertThat(savedReservation.getRoom().getName()).isEqualTo(this.room.getName());
        assertThat(savedReservation.getId()).isEqualTo(room.getId() + "||" + TimeParsingUtils.formatterString(sTime));

    }

    @Test
    public void givenRoomIdAndTimePeriod_whenFindByIdAndTimePeriod_thenReturnReservationList() {
        // given - precondition or setup
        String startTime = "2022-04-24 10:30:00";
        String endTime = "2022-04-24 12:30:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        getReservation();
        // when - action or the behaviour that we are going test
        List<Reservation> reservationsByRoomIdAndTime = reservationRepository.findByRoomIdAndTimePeriod(this.room.getId(), sTime, eTime);
        log.info("reservationsByRoomIdAndTime: {}", reservationsByRoomIdAndTime);

        // then - verify the output
        assertThat(reservationsByRoomIdAndTime).isNotNull();
        assertThat(reservationsByRoomIdAndTime.size()).isEqualTo(1);

    }

    @Test
    public void givenRoomId_whenFindById_thenReturnReservationList() {
        // given - precondition or setup
        getReservation();

        // when - action or the behaviour that we are going test
        List<Reservation> reservationsByRoomId = reservationRepository.findByRoomId(this.room.getId());
        log.info("reservationsByRoomId: {}", reservationsByRoomId);

        // then - verify the output
        assertThat(reservationsByRoomId).isNotNull();
        assertThat(reservationsByRoomId.size()).isEqualTo(1);

    }

    @Test
    public void givenUserId_whenFindById_thenReturnReservationList() {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        User savedUser = userRepository.save(user);
        Room savedRoom = roomRepository.save(room);
        userGroup.setUser(savedUser);

        String startTime = "2022-04-24 10:30:00";
        String endTime = "2022-04-24 12:30:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        IntStream.rangeClosed(1, 4).forEach(i -> {
            userGroup.setStudyGroup(savedStudyGroup);
            userGroupRepository.save(userGroup);
            Reservation reservation = Reservation.builder()
                    .id(room.getId() + "||" + TimeParsingUtils.formatterString(sTime))
                    .user(savedUser)
                    .startTime(sTime)
                    .endTime(eTime)
                    .studyGroup(savedStudyGroup)
                    .room(savedRoom)
                    .build();
            reservationRepository.save(reservation);
        });

        // when - action or the behaviour that we are going test
        List<Long> sgIds = userGroupRepository.findSgIdsByUserId(savedUser.getId());
        List<Reservation> reservationsBySGIds = reservationRepository.findByStudyGroupIds(sgIds);

        log.info("reservationsByStudyGroupIdIn: {}", reservationsBySGIds);

        // then - verify the output
        assertThat(reservationsBySGIds).isNotNull();
//        assertThat(reservationsBySGIds.size()).isEqualTo(4);

    }

    @Test
    public void givenRoomIdAndReservationId_whenFindById_thenReturnReservation() {
        // given - precondition or setup
        Reservation savedReservation = getReservation();

        // when - action or the behaviour that we are going test
        Reservation findReservation = reservationRepository.findByRoomIdAndId(this.room.getId(), savedReservation.getId()).get();
        log.info("findReservation: {}", findReservation);

        // then - verify the output
        assertThat(findReservation).isNotNull();

    }

    @Test
    public void givenReservation_whenUpdate_thenReturnUpdatedReservationId() {
        // given - precondition or setup
        Reservation savedReservation = getReservation();
        Reservation oldReservation = reservationRepository.findById(savedReservation.getId()).get();

        Reservation newReservation = getReservation();

        // when - action or the behaviour that we are going test
        Reservation updatedReservation = reservationRepository.save(newReservation);
        reservationRepository.delete(oldReservation);
        // then - verify the output
        assertThat(updatedReservation.getId()).isEqualTo(newReservation.getId());

    }

    @Test
    public void givenReservation_whenDelete_thenRemoveReservation() {
        // given - precondition or setup
        Reservation savedReservation = getReservation();

        // when - action or the behaviour that we are going test
        Reservation findReservation = reservationRepository.findById(savedReservation.getId()).get();
        reservationRepository.delete(findReservation);

        // then - verify the output
        assertThat(reservationRepository.findById(savedReservation.getId())).isEmpty();

    }

    private Reservation getReservation() {
        User savedUser = userRepository.save(user);
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        Room savedRoom = roomRepository.save(room);

        String startTime = "2022-04-24 10:30:00";
        String endTime = "2022-04-24 12:30:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        Reservation reservation = Reservation.builder()
                .id(room.getId() + "||" + TimeParsingUtils.formatterString(sTime))
                .user(savedUser)
                .startTime(sTime)
                .endTime(eTime)
                .studyGroup(savedStudyGroup)
                .room(savedRoom)
                .build();

        return reservationRepository.save(reservation);
    }


    @Test
    public void givenSaveReservation_whenFindBy_thenPageOptional() {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        User savedUser = userRepository.save(user);
        Room savedRoom = roomRepository.save(room);
        userGroup.setUser(savedUser);

        String startTime = "2022-04-24 10:30:00";
        String endTime = "2022-04-24 12:30:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        IntStream.rangeClosed(1, 10).forEach(i -> {
            userGroup.setStudyGroup(savedStudyGroup);
            userGroupRepository.save(userGroup);
            Reservation reservation = Reservation.builder()
                    .id(room.getId() + "||" + TimeParsingUtils.formatterString(sTime))
                    .user(savedUser)
                    .startTime(sTime)
                    .endTime(eTime)
                    .studyGroup(savedStudyGroup)
                    .room(savedRoom)
                    .build();
            reservationRepository.save(reservation);
        });

        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());

        // when - action or the behaviour that we are going test
        Page<Reservation> reservationPage = reservationRepository.findBy(pageable);

        log.info("reservationPage: {}", reservationPage);
        log.info("reservationPage.getContent(): {}", reservationPage.getContent());
        reservationPage.get().forEach(System.out::println);
        // then - verify the output
        assertThat(reservationPage.getContent()).isNotNull();

    }
}
