package app.producer;

import app.config.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class WikimediaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebClient wikimediaWebClient;

    public void sendMessage(String message) {
        kafkaTemplate.send(KafkaTopics.WIKIMEDIA_UPDATES, message);
    }

    public void consumeMessage() {
        wikimediaWebClient.get()
                .uri("/stream/recentchange")
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(s -> {
                    log.info("Received message: {}", s);
                    sendMessage(s);
                });
    }
}
