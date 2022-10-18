package com.dsg.wardstudy.common.adapter.kafka;

import com.dsg.wardstudy.common.adapter.MailMessageGenerator;
import com.dsg.wardstudy.common.adapter.MailSendService;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.reservation.service.ReservationService;
import com.dsg.wardstudy.type.UserType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JsonKafkaConsumer {

    private final ReservationService reservationService;
    private final MailMessageGenerator messageGenerator;
    private final MailSendService mailSendService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Reservation savedReservation) {
        LOGGER.info(String.format("Json message received -> %s", savedReservation.toString()));

/*        savedReservation.getStudyGroup().getUserGroups().stream()
                .filter(ug -> ug.getUserType().equals(UserType.PARTICIPANT))
                .map(ug -> {
                    if (ug.getUser().getEmail().isEmpty()) {
                        String toMessage = messageGenerator.toMessage(ug.getUser().getName(), ug.getUser().getReservations());
                        LOGGER.info("sendMail: {}", toMessage);
                        if (mailSendService.sendMail(ug.getUser().getEmail(), "ward-study 예약룸 알림", toMessage)) {
                            for (Reservation r : ug.getUser().getReservations()) {
                                reservationService.changeIsEmailSent(r);
                            }
                        }
                    }

                    return null;
                }).collect(Collectors.toList());*/

    }
}
