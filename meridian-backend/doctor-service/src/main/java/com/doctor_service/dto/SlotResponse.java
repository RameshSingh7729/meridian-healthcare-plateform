
package com.doctor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotResponse {

    private Long slotId;

    private LocalDate appointmentDate;

    private LocalTime slotTime;

    private String status;
}
