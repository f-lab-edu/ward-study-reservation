package com.dsg.wardstudy;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class WardStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WardStudyApplication.class, args);
    }

}
