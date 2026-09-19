package notification_service.service;

import notification_service.dto.AppointmentBookedEvent;

public interface EmailService {

    void sendAppointmentConfirmation(AppointmentBookedEvent event);

}