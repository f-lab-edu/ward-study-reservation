package com.dsg.wardstudy;

import com.dsg.wardstudy.common.utils.Encryptor;
import com.dsg.wardstudy.config.jpa.JpaAuditingConfig;
import com.dsg.wardstudy.domain.reservation.entity.Reservation;
import com.dsg.wardstudy.domain.reservation.entity.Room;
import com.dsg.wardstudy.domain.studyGroup.entity.StudyGroup;
import com.dsg.wardstudy.domain.user.constant.UserType;
import com.dsg.wardstudy.domain.user.entity.User;
import com.dsg.wardstudy.domain.user.entity.UserGroup;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.reservation.RoomRepository;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.namics.commons.random.RandomData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.stream.LongStream;

@Slf4j
@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class InsertTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

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
                .password(Encryptor.encrypt("1234"))
                .build();

        userGroup = UserGroup.builder()
                .userType(UserType.LEADER)
                .build();

        studyGroup = StudyGroup.builder()
                .title("title_")
                .content("title_study 번 방")
                .build();

        room = Room.builder()
                .name("roomA")
                .build();

        userRepository.save(user);
        userGroupRepository.save(userGroup);
        studyGroupRepository.save(studyGroup);
        roomRepository.save(room);
    }

    @Test
    public void test() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        LongStream.rangeClosed(1, 28*10000).forEach( i -> {
            Reservation reservation = Reservation.builder()
                    .startTime(random())
                    .endTime(random())
                    .user(user)
                    .studyGroup(studyGroup)
                    .room(room)
                    .build();

            reservationRepository.save(reservation);

            }
        );
        stopWatch.stop();
        log.info("객체 생성 시간: {}", stopWatch.getTotalTimeSeconds());

    }

    @Test
    public void test2() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        LongStream.rangeClosed(1, 28*10000).forEach( i -> {
            StudyGroup studyGroup = StudyGroup.builder()
                    .title("test_title_"+i)
                    .content("test_content_"+i)
                    .build();
            studyGroupRepository.save(studyGroup);

            }
        );
        stopWatch.stop();
        log.info("객체 생성 시간: {}", stopWatch.getTotalTimeSeconds());

    }

    public LocalDateTime random() {
        LocalDateTime now = LocalDateTime.now();
        int year = 60 * 60 * 24 * 365;
        return now.plusSeconds((long) RandomData.randomInteger(-2 * year, 2 * year));
    }

}
