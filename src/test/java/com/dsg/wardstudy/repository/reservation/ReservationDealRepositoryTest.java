package com.dsg.wardstudy.repository.reservation;

import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.ReservationDeal;
import com.dsg.wardstudy.type.Status;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
class ReservationDealRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationDealRepository reservationDealRepository;

    private Reservation reservation;
    private ReservationDeal deal;

    @BeforeEach
    void setup() {

        reservation = Reservation.builder()
                .id("2||2022-05-18 10:30:00")
                .startTime(LocalDateTime.of(2022, Month.MAY, 19, 6, 30))
                .endTime(LocalDateTime.of(2022, Month.MAY, 19, 7, 30))
                .build();

        deal = ReservationDeal.builder()
                .id(1L)
                .dealDate(LocalDateTime.now())
                .status(Status.ENABLED)
                .reservation(reservation)
                .build();
    }



    @Test
    public void givenReservationAndDeal_whenFindBy_thenFindDeal(){
        // given - precondition or setup
        reservationRepository.save(reservation);
        ReservationDeal save = reservationDealRepository.save(deal);
        log.info("save: {}", save);
        // when - action or the behaviour that we are going test
        ReservationDeal findDeal = reservationDealRepository.findById(1L).get();
        log.info("findDeal: {}", findDeal);
        // then - verify the output
        assertThat(findDeal).isNotNull();
        assertThat(findDeal.getStatus()).isEqualTo(Status.ENABLED);

    }

    @Test
    public void givenReservationAndDeal_whenFindByReservationId_thenFindDeal(){
        // given - precondition or setup
        reservationRepository.save(reservation);
        ReservationDeal save = reservationDealRepository.save(deal);
        log.info("save: {}", save);
        // when - action or the behaviour that we are going test
        ReservationDeal findDeal = reservationDealRepository.findByReservationId(reservation.getId()).get();
        log.info("findDeal: {}", findDeal);
        // then - verify the output
        assertThat(findDeal).isNotNull();
        assertThat(findDeal.getStatus()).isEqualTo(Status.ENABLED);

    }

}