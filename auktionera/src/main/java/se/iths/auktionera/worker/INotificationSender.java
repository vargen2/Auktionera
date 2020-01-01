package se.iths.auktionera.worker;

import se.iths.auktionera.business.model.EmailNotification;

public interface INotificationSender {
    void enqueueEmailNotification(EmailNotification emailNotification);
}
