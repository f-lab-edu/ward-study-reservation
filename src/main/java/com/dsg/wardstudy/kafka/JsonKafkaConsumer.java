package com.dsg.wardstudy.kafka;

import com.dsg.wardstudy.dto.reservation.ReservationDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic-json.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ReservationDetails details){
        LOGGER.info(String.format("Json message recieved -> %s", details.toString()));
    }
}
