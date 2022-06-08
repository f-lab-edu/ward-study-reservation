package com.dsg.wardstudy;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableBatchProcessing
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class WardStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WardStudyApplication.class, args);
    }

    // hibernateLazyInitializer 제거용 라이브러리
    @Bean
    Hibernate5Module hibernate5Module(){
        return new Hibernate5Module();
    }
}
