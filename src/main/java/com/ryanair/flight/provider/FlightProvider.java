package com.ryanair.flight.provider;

import com.ryanair.flight.domain.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightProvider {
    List<Flight> getFlights(String departure, String arrival, LocalDateTime departureDataTime, LocalDateTime arrivalDataTime);
}
