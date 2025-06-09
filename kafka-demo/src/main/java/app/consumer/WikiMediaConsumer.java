package app.consumer;

import app.config.WebClientConfig;
import app.producer.WikimediaProducer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class WikiMediaConsumer {

    @KafkaListener(topics = "wikimedia-updates", groupId = "my-kafka-group")
    public void consumeMessage(String message) {
        // Log the received message
        log.info("Received message: {}", message);
    }

}
