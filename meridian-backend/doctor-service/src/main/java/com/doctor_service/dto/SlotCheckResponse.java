
package com.doctor_service.dto;



import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SlotCheckResponse {

    private Long availabilityId;

    private Long doctorId;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer slotDuration;

    private List<SlotResponse> slots;

}
