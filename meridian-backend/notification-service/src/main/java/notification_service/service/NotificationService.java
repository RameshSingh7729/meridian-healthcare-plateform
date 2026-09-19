package notification_service.service;

import notification_service.dto.AppointmentBookedEvent;

public interface NotificationService {

    void processNotification(AppointmentBookedEvent event);

}