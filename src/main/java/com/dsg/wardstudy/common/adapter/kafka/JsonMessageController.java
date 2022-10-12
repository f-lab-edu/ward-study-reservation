package com.dsg.wardstudy.common.adapter.kafka;

import com.dsg.wardstudy.domain.reservation.dto.ReservationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kafka")
public class JsonMessageController {

    private final JsonKafkaProducer kafkaProducer;

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody ReservationDetails details){
        kafkaProducer.sendMessage(details);
        return ResponseEntity.ok("Json message sent to kafka topic");
    }
}
