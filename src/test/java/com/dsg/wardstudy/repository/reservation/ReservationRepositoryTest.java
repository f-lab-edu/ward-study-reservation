package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
class ReservationRepositoryTest {

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
                .id(1L)
                .email("dsgfunk@gmail.com")
                .build();

        userGroup = UserGroup.builder()
                .userType(UserType.P)
                .build();

        studyGroup = StudyGroup.builder()
                .id(1L)
                .title("title_")
                .content("study 번 방")
                .build();

        room = Room.builder()
                .id(1L)
                .name("roomA")
                .build();

    }

    @Test
    public void givenReservation_whenSave_thenReturnSavedReservation() {
        // given - precondition or setup
        User savedUser = userRepository.save(user);
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        Room savedRoom = roomRepository.save(room);

        String startTime = "2021-08-07 12:00:00";
        String endTime = "2021-08-07 13:00:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        Reservation reservation = Reservation.builder()
                .id("3||2022-04-24 10:30:00")
                .startTime(sTime)
                .endTime(eTime)
                .user(savedUser)
                .studyGroup(savedStudyGroup)
                .room(savedRoom)
                .build();

        // when - action or the behaviour that we are going test
        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("savedReservation: {}", savedReservation);

        Reservation reservation1 = reservationRepository.findById(savedReservation.getId()).get();
        log.info("reservation1: {}", reservation1);
        // then - verify the output
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getId()).isEqualTo("3||2022-04-24 10:30:00");
        assertThat(savedReservation.getStartTime()).isEqualTo(sTime);
        assertThat(savedReservation.getUser().getEmail()).isEqualTo(this.user.getEmail());
        assertThat(savedReservation.getStudyGroup().getTitle()).isEqualTo(this.studyGroup.getTitle());
        assertThat(savedReservation.getRoom().getName()).isEqualTo(this.room.getName());

    }

    @Test
    public void given_whenFindAll_thenReservationList(){
        // given - precondition or setup
        String startTime = "2021-08-07 12:00:00";
        String endTime = "2021-08-07 13:00:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        LongStream.rangeClosed(1,20).forEach(i -> {
            Reservation reservation = Reservation.builder()
                    .id("i_"+i)
                    .startTime(sTime)
                    .endTime(eTime)
                    .build();

            reservationRepository.save(reservation);
        });
        // when - action or the behaviour that we are going test
        List<Reservation> all = reservationRepository.findAll();
        log.info("all: {}", all);
        log.info("all.size(): {}", all.size());
        // then - verify the output
        assertThat(all.size()).isNotNull();
        assertThat(all.size()).isEqualTo(20);

    }

    @Test
    public void givenRoomIdAndTimePeriod_whenFindByIdAndTimePeriod_thenReturnReservationList() {
        // getByRoomIdAndTimePeriod
        // given - precondition or setup
        User savedUser = userRepository.save(user);
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        Room savedRoom = roomRepository.save(room);

        String startTime = "2021-08-07 12:00:00";
        String endTime = "2022-08-07 13:00:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);

        Reservation reservation = Reservation.builder()
                .id("3||2022-08-07 12:30:00")
                .user(savedUser)
                .startTime(sTime)
                .endTime(eTime)
                .studyGroup(savedStudyGroup)
                .room(savedRoom)
                .build();

        reservationRepository.save(reservation);

        // when - action or the behaviour that we are going test
        List<Reservation> reservationsByRoomIdAndTime =
                reservationRepository.findByRoomIdAndTimePeriod(savedRoom.getId(), sTime, eTime);
        log.info("reservationsByRoomIdAndTime: {}", reservationsByRoomIdAndTime);

        // then - verify the output
        assertThat(reservationsByRoomIdAndTime).isNotNull();
        assertThat(reservationsByRoomIdAndTime.size()).isEqualTo(1);

    }

    @Test
    public void givenRoomId_whenFindById_thenReturnReservationList() {
        // getByRoomId
        // given - precondition or setup
        Room savedRoom = roomRepository.save(room);

        Reservation reservation = Reservation.builder()
                .id("3||2022-04-24 10:30:00")
                .room(savedRoom)
                .build();

        reservationRepository.save(reservation);

        // when - action or the behaviour that we are going test
        List<Reservation> reservationsByRoomId = reservationRepository.findByRoomId(savedRoom.getId());
        log.info("reservationsByRoomId: {}", reservationsByRoomId);

        // then - verify the output
        assertThat(reservationsByRoomId).isNotNull();
        assertThat(reservationsByRoomId.size()).isEqualTo(1);

    }

    @Test
    public void givenUserId_whenFindById_thenReturnReservationList() {
        // getAllByUserId
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        User savedUser = userRepository.save(user);

        userGroup.setUser(savedUser);

        IntStream.rangeClosed(1, 10).forEach(i -> {
            userGroup.setStudyGroup(savedStudyGroup);
            userGroupRepository.save(userGroup);
            Reservation reservation = Reservation.builder()
                    .id("3||2022-04-0" + i + " 10:30:00")
                    .studyGroup(savedStudyGroup)
                    .build();
            reservationRepository.save(reservation);
        });

        // when - action or the behaviour that we are going test
        List<Long> sgIds = userGroupRepository.findSgIdsByUserId(savedUser.getId());
        List<Reservation> reservationsBySGIds = reservationRepository.findByStudyGroupIds(sgIds);

        log.info("reservationsBySGIds: {}", reservationsBySGIds);

        // then - verify the output
        assertThat(reservationsBySGIds).isNotNull();
        assertThat(reservationsBySGIds.size()).isEqualTo(10);

    }

    @Test
    public void givenRoomIdAndReservationId_whenFindById_thenReturnReservation() {
        // findByRoomIdAndId
        // given - precondition or setup
        Room savedRoom = roomRepository.save(room);

        Reservation reservation = Reservation.builder()
                .id("1||2022-11-03 06:30:00")
                .room(savedRoom)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("savedReservation: {}", savedReservation);

        // when - action or the behaviour that we are going test
        Reservation findReservation = reservationRepository.findByRoomIdAndId(savedRoom.getId(), savedReservation.getId()).get();
        log.info("findReservation: {}", findReservation);

        // then - verify the output
        assertThat(findReservation).isNotNull();

    }

    @Test
    public void givenReservation_whenUpdate_thenReturnUpdatedReservationId() {
        // given - precondition or setup
        Room savedRoom = roomRepository.save(room);

        String startTime = "2021-08-07 12:00:00";

        Reservation reservation = Reservation.builder()
                .id("1||2022-11-03 06:30:00")
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        Reservation oldReservation = reservationRepository.findById(savedReservation.getId()).get();
        Room findRoom = roomRepository.findById(savedRoom.getId()).get();
        log.info("oldReservation: {}, findRoom: {}", oldReservation, findRoom);

        Reservation newReservation = Reservation.builder()
                .id(findRoom.getId() + "||" + startTime)
                .build();

        // when - action or the behaviour that we are going test
        Reservation updatedReservation = reservationRepository.save(newReservation);
        log.info("updatedReservation: {}", updatedReservation);
        reservationRepository.delete(oldReservation);

        // then - verify the output
        assertThat(updatedReservation.getId()).isEqualTo(newReservation.getId());

    }

    @Test
    public void givenReservation_whenDelete_thenRemoveReservation() {
        // given - precondition or setup
        Reservation reservation = Reservation.builder()
                .id("3||2022-04-24 10:30:00")
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        // when - action or the behaviour that we are going test
        Reservation findReservation = reservationRepository.findById(savedReservation.getId()).get();
        reservationRepository.delete(findReservation);

        // then - verify the output
        assertThat(reservationRepository.findById(savedReservation.getId())).isEmpty();

    }

    @Test
    public void givenSaveReservation_whenFindBy_thenPageOptional(){
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        User savedUser = userRepository.save(user);

        userGroup.setUser(savedUser);

        IntStream.rangeClosed(1, 10).forEach(i -> {
            userGroup.setStudyGroup(savedStudyGroup);
            userGroupRepository.save(userGroup);
            Reservation reservation = Reservation.builder()
                    .id("3||2022-04-0" + i + " 10:30:00")
                    .studyGroup(savedStudyGroup)
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