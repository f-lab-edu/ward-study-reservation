package com.dsg.wardstudy.config.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;

    private final Job notificationAlarmJob;

    @Scheduled(cron = "0 0/30 * * * *")
    public void runJob() {

        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("timestamp", new JobParameter(System.currentTimeMillis()));

        try {
            jobLauncher.run(notificationAlarmJob, new JobParameters(parameters));
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
