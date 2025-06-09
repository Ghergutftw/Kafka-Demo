package app.producer;

import app.config.WebClientConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class WikimediaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebClientConfig webClientConfig;
    private final WikimediaProducer wikimediaProducer;

    public void sendMessage(String message) {
        kafkaTemplate.send("wikimedia-updates", message);
    }


    public void consumeMessage() {
        webClientConfig.webClientBuilder()
                .get()
                .uri("/stream/recentchange")
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(s -> {
                    log.info("Received message: {}", s);
                    wikimediaProducer.sendMessage(s);
                });
    }
}
