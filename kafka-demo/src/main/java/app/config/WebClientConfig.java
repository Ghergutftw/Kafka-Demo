package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    public WebClient webClientBuilder() {
        return WebClient.builder().baseUrl("https://stream.wikimedia.org/v2").build();
    }

}
