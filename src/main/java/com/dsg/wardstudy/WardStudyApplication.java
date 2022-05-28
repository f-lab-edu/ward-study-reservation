package com.dsg.wardstudy;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@SpringBootApplication
@EnableScheduling
public class WardStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WardStudyApplication.class, args);
    }

}
