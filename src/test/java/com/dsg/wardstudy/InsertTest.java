package com.dsg.wardstudy;

import com.dsg.wardstudy.config.jpa.JpaAuditingConfig;
import com.dsg.wardstudy.domain.reservation.entity.Reservation;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.namics.commons.random.RandomData;
import lombok.extern.slf4j.Slf4j;
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

    @Test
    public void test() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        LongStream.rangeClosed(1, 100*10000).forEach( i -> {
            Reservation reservation = Reservation.builder()
                    .startTime(random())
                    .endTime(random())
                    .build();
            reservationRepository.save(reservation);

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
