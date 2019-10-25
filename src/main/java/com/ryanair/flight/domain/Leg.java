package com.ryanair.flight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leg {
    String departureAirport;
    String arrivalAirport;
    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;
}
