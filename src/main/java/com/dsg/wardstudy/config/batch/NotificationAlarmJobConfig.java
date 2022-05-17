package com.dsg.wardstudy.config.batch;


import com.dsg.wardstudy.adapter.MailSendService;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.dto.NotificationAlarmDto;
import com.dsg.wardstudy.dto.UserDto;
import com.dsg.wardstudy.repository.reservation.ReservationRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NotificationAlarmJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ReservationRepository reservationRepository;
    private final UserGroupRepository userGroupRepository;

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
    public Step notificationAlarmStep(ItemReader<Reservation> notificationAlarmReader,
                                      ItemProcessor<Reservation, NotificationAlarmDto> notificationAlarmProcessor,
                                      ItemWriter<NotificationAlarmDto> notificationAlarmWriter) {
        return stepBuilderFactory.get("notificationAlarmStep")
                .<Reservation, NotificationAlarmDto>chunk(CHUNK_SIZE)
                .reader(notificationAlarmReader)
                .processor(notificationAlarmProcessor)
                .writer(notificationAlarmWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Reservation> notificationAlarmReader() {
        return new RepositoryItemReaderBuilder<Reservation>()
                .name("notificationAlarmReader")
                .repository(reservationRepository)
                .methodName("findBy")
                .pageSize(CHUNK_SIZE)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Reservation, NotificationAlarmDto> notificationAlarmProcessor() {
        return reservation -> {
            // TODO : 해당 스터디그룹 users -> user.getEmail에 message 기입
            List<User> users = userGroupRepository.findUserBySGId(reservation.getStudyGroup().getId());

            List<UserDto> userDtos = users.stream()
                    .map(u -> UserDto.builder()
                            .name(u.getName())
                            .email(u.getEmail())
                            .build()
                    )
                    .collect(Collectors.toList());

            return NotificationAlarmDto.builder()
                    .id(reservation.getId())
                    .startTime(reservation.getStartTime())
                    .endTime(reservation.getEndTime())
                    .userDtos(userDtos)
                    .studyGroup(reservation.getStudyGroup())
                    .room(reservation.getRoom())
                    .build();
        };
    }

    @StepScope
    @Bean
    public ItemWriter<NotificationAlarmDto> notificationAlarmWriter(MailSendService mailSendService) {
        return items -> items.forEach(
                item -> {
                    log.info("message: {}", item.toMessage());
                    mailSendService.sendMail("ehtjd33@gmail.com", item.getId(), item.toMessage());
                }
        );
    }

}
