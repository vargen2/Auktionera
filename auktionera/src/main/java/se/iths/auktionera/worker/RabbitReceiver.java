package se.iths.auktionera.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.EmailNotification;
import se.iths.auktionera.config.RabbitConfig;

@Service
public class RabbitReceiver {

    private static final Logger log = LoggerFactory.getLogger(RabbitReceiver.class);

    @RabbitListener(queues = RabbitConfig.emailNotificationQueue)
    public void receiveMessage(EmailNotification notification) {
        log.info("Received emailNotification: {}", notification.toString());
    }

}
