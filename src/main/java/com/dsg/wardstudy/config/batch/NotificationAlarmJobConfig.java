package com.dsg.wardstudy.config.batch;


import com.dsg.wardstudy.adapter.MailSendService;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.dsg.wardstudy.dto.NotificationAlarmDto;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Reservation, NotificationAlarmDto> notificationAlarmProcessor() {
        return reservation -> {
            // todo들 아직 compleated 안된 거 count로 표기
/*            List<String> userEmails = userGroupRepository.findUserBySGId(1L).stream()
                    .map(user -> user.getEmail())
                    .collect(Collectors.toList());

            List<Reservation> reservationList = reservationRepository.findAll();

            if (reservationList.isEmpty()) {
                return null;
            }*/

            return NotificationAlarmDto.builder()
                    .id(reservation.getId())
                    .startTime(reservation.getStartTime())
                    .endTime(reservation.getEndTime())
//                    .userId(reservation.getUser().getId())
//                    .studyGroupId(reservation.getStudyGroup().getId())
//                    .roomId(reservation.getRoom().getId())
                    .build();
        };
    }

/*    @StepScope
    @Bean
    public ItemWriter<NotificationAlarmDto> todoNotificationWriter() {
        return items -> {
            items.forEach(System.out::println);
            System.out.println("==== chunk is finished");
        };
    }*/

    @StepScope
    @Bean
    public ItemWriter<NotificationAlarmDto> notificationAlarmWriter(MailSendService mailSendService) {
        return items -> items.forEach(
                item -> mailSendService.sendMail("ehtjd33@gmail.com", item.getId(), item.toMessage())
        );
    }

}
