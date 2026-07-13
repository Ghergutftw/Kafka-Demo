package app.consumer;

import app.config.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WikiMediaConsumer {

    @KafkaListener(topics = KafkaTopics.WIKIMEDIA_UPDATES, groupId = KafkaTopics.GROUP_ID)
    public void consumeMessage(String message) {
        log.info("Received message: {}", message);
    }

}
