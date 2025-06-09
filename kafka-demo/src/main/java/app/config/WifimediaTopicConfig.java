package app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class WifimediaTopicConfig {

    @Bean
    public NewTopic wikiMedia() {
        return TopicBuilder.name("wikimedia-updates")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
