package app.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    //read from applicaiton.yml
    @KafkaListener(topics = "my-topic", groupId = "my-kafka-group")
    public void consumeMessage(String message) {
        // Log the received message
        log.info("Received message: {}", message);
    }

}
