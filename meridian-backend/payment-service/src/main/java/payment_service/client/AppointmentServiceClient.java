package payment_service.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import payment_service.dto.AppointmentResponse;

import java.util.UUID;

@FeignClient(name = "APPOINTMENT-BOOKING-SERVICE")
public interface AppointmentServiceClient {

    @GetMapping("/api/appointments/{appointmentId}")
    AppointmentResponse getAppointment(
            @PathVariable UUID appointmentId);

    @PutMapping("/api/appointments/{appointmentId}/confirm")
    AppointmentResponse confirmAppointment(
            @PathVariable UUID appointmentId);
}
