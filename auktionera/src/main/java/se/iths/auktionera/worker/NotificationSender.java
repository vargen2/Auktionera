package se.iths.auktionera.worker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import se.iths.auktionera.AuktioneraApplication;
import se.iths.auktionera.business.model.EmailNotification;

@Service
public class NotificationSender implements INotificationSender {
    //TODO rename till något med TaskExecutor
    private final TaskExecutor taskExecutor;
    private final RabbitTemplate rabbitTemplate;

    public NotificationSender(TaskExecutor taskExecutor, RabbitTemplate rabbitTemplate) {
        this.taskExecutor = taskExecutor;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enqueueEmailNotification(EmailNotification emailNotification) {
        taskExecutor.execute(() -> rabbitTemplate.convertAndSend(AuktioneraApplication.topicExchangeName, "auktionera.events.newReview", emailNotification));
    }
}
