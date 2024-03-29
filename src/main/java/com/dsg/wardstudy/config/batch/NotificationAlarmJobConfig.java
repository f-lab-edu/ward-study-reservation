package com.dsg.wardstudy.config.batch;


import com.dsg.wardstudy.common.adapter.mail.MailMessageGenerator;
import com.dsg.wardstudy.common.adapter.mail.MailSendService;
import com.dsg.wardstudy.domain.reservation.entity.Reservation;
import com.dsg.wardstudy.domain.reservation.dto.NotificationAlarmDto;
import com.dsg.wardstudy.domain.reservation.service.ReservationService;
import com.dsg.wardstudy.domain.user.entity.User;
import com.dsg.wardstudy.repository.reservation.ReservationQueryRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Log4j2
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class NotificationAlarmJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ReservationService reservationService;
    private final ReservationQueryRepository reservationQueryRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final MailSendService mailSendService;
    private final MailMessageGenerator messageGenerator;

    private static final int CHUNK_SIZE = 4;

    @Bean("notificationAlarmJob")
    public Job notificationAlarmJob(Step notificationAlarmStep) {
        return jobBuilderFactory.get("notificationAlarmJob")
                .incrementer(new RunIdIncrementer())
                .start(notificationAlarmStep)
                .build();
    }

    @JobScope
    @Bean("notificationAlarmStep")
    public Step notificationAlarmStep(ItemReader<User> notificationAlarmReader,
                                      ItemProcessor<User, NotificationAlarmDto> notificationAlarmProcessor,
                                      ItemWriter<NotificationAlarmDto> notificationAlarmWriter) {
        return stepBuilderFactory.get("notificationAlarmStep")
                .<User, NotificationAlarmDto>chunk(CHUNK_SIZE)
                .reader(notificationAlarmReader)
                .processor(notificationAlarmProcessor)
                .writer(notificationAlarmWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<User> notificationAlarmReader() {
        return new RepositoryItemReaderBuilder<User>()
                .name("notificationAlarmReader")
                .repository(userRepository)
                .methodName("findBy")
                .pageSize(CHUNK_SIZE)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<User, NotificationAlarmDto> notificationAlarmProcessor() {
        return user -> {

            List<Long> sgIds = userGroupRepository.findSgIdsByUserId(user.getId());
            log.info("sgIds: {}", sgIds);

            List<Reservation> reservations =
                    // IsEmailSent.eq(false)인 메일 축출 추가
                    reservationQueryRepository.findByStartTimeAfterNowAndIsSentFalse(sgIds);
            log.info("reservations: {}", reservations);

            return NotificationAlarmDto.builder()
                    .email(user.getEmail())
                    .userName(user.getName())
                    .reservations(reservations)
                    .build();
        };
    }

    @StepScope
    @Bean
    public ItemWriter<NotificationAlarmDto> notificationAlarmWriter() {
        return items -> items.forEach(
                item -> {
                    if(!item.getReservations().isEmpty()){
                        String toMessage = messageGenerator.toMessage(item.getUserName(), item.getReservations());
                        log.info("sendMail: {}", toMessage);
                        if(mailSendService.sendMail(item.getEmail(), "ward-study 예약룸 알림", toMessage)) {
                            for (Reservation r : item.getReservations()) {
                                reservationService.changeIsEmailSent(r);
                            }
                        }
                    }
                }
        );
    }

}
