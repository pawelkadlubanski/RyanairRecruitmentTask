package com.ryanair.flight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDays {
    private Integer day;
    private List<ScheduledFlight> scheduledFlights;
}
