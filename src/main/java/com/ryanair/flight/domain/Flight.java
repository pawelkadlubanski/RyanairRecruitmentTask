package com.ryanair.flight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private String number;
    private String departureTime;
    private String arrivalTime;
}
