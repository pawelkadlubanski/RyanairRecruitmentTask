package com.ryanair.flight.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rout {
    private String airportFrom;
    private String airportTo;
    private String connectingAirport;
    private Boolean newRoute;
    private Boolean seasonalRoute;
    private String operator;
    private String group;
}
