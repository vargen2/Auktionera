package se.iths.auktionera.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String topicExchangeName = "auktionera-exchange";

    public static final String emailNotificationQueue = "auktionera-emailNotificationQueue";

    @Bean
    Queue queue() {
        return new Queue(emailNotificationQueue, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding newBidBinding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("auktionera.events.#");
    }

}
