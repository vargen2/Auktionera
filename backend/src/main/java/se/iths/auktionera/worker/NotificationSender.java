package se.iths.auktionera.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import se.iths.auktionera.business.model.EmailNotification;
import se.iths.auktionera.config.RabbitConfig;

@Service
public class NotificationSender implements INotificationSender {

    private static final Logger log = LoggerFactory.getLogger(NotificationSender.class);

    private final TaskExecutor taskExecutor;
    private final RabbitTemplate rabbitTemplate;

    public NotificationSender(TaskExecutor taskExecutor, RabbitTemplate rabbitTemplate) {
        this.taskExecutor = taskExecutor;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enqueueEmailNotification(EmailNotification emailNotification) {
        log.info("Enqueue emailNotification {}", emailNotification);
        taskExecutor.execute(() -> rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, "auktionera.events.newReview", emailNotification));
    }
}
