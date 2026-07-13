package app.producer;

import app.config.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

//    Needs to match the Template type from application.yml, for JSON you have to change
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        log.info("Producing message: {} to {}", message, KafkaTopics.MY_TOPIC);
        kafkaTemplate.send(KafkaTopics.MY_TOPIC, message);
    }

}
