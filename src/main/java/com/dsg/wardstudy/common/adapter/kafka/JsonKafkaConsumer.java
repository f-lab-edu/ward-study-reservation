package com.dsg.wardstudy.common.adapter.kafka;

import com.dsg.wardstudy.common.adapter.mail.MailMessageGenerator;
import com.dsg.wardstudy.common.adapter.mail.MailSendService;
import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.domain.reservation.entity.Reservation;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.domain.user.constant.UserType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JsonKafkaConsumer {
    private final UserGroupRepository userGroupRepository;
    private final MailMessageGenerator messageGenerator;
    private final MailSendService mailSendService;
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Reservation savedReservation) {
        LOGGER.info(String.format("Json message received -> %s", savedReservation.toString()));
        // 비동기 통신 구현 -> 향후 Reservation 도메인에서 다른 프로젝트 서비스단에서 비동기적으로 이메일 알람 구현 가능케 해야
        // for Reservation 도메인에서 트래픽 부하를 저하
        Long sgId = savedReservation.getStudyGroup().getId();
        userGroupRepository.findUserBySGId(sgId).forEach(
                user -> {
                    UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(user.getId(), sgId)
                            .orElseThrow(() -> new WSApiException(ErrorCode.NO_FOUND_ENTITY));
                    if ( UserType.PARTICIPANT.equals(userType) && !user.getEmail().isEmpty()) {
                        String toMessage = messageGenerator.toKafkaMessage(user.getName(), savedReservation);
                        LOGGER.info("sendMail: {}", toMessage);
                        mailSendService.sendMail(user.getEmail(), "ward-study 예약룸 알림", toMessage);
                    }
                }
        );

    }
}
