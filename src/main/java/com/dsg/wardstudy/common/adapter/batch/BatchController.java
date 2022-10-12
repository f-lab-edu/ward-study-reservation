package com.dsg.wardstudy.common.adapter.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job notificationAlarmJob;

    @GetMapping("/job")
    public String startJob() throws Exception {
        System.out.println("Starting the batch job");
        System.out.println("job: " +notificationAlarmJob);

        Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("timestamp", new JobParameter(System.currentTimeMillis()));
        JobExecution jobExecution = jobLauncher.run(notificationAlarmJob, new JobParameters(parameters));
        System.out.println("Batch job "+ jobExecution.getStatus());

        return "Batch job "+ jobExecution.getStatus();

    }
}
