package app.controller;

import app.dto.Package;
import app.producer.KafkaJsonProducer;
import app.producer.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {

    private final KafkaProducer kafkaProducer;
    private final KafkaJsonProducer kafkaJsonProducer;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent successfully: " + message);
    }

    @PostMapping("/send-json")
    public ResponseEntity<String> sendJsonMessage(@RequestBody Package pkg) {
        kafkaJsonProducer.sendPackage(pkg);
        return ResponseEntity.ok("JSON message sent successfully");
    }

}
