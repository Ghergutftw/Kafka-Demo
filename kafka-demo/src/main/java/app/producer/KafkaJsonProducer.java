package app.producer;

import app.config.KafkaTopics;
import app.dto.Package;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaJsonProducer {

    private final KafkaTemplate<String, Package> kafkaTemplate;

    public void sendPackage(Package pkg) {
        Message<Package> message = MessageBuilder
                .withPayload(pkg)
                .setHeader(KafkaHeaders.TOPIC, KafkaTopics.MY_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }
}
