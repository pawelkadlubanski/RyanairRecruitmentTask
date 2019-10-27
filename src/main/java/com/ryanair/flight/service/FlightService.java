package com.ryanair.flight.service;

import com.ryanair.flight.domain.Flight;
import com.ryanair.flight.provider.DirectFlightProvider;
import com.ryanair.flight.provider.IndirectFlightProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class FlightService {
    private final DirectFlightProvider directFlightProvider;
    private final IndirectFlightProvider indirectFlightProvider;

    public List<Flight> getFlights(String departure, String arrival, String departureTime, String arrivalTime) {
        LocalDateTime departureDataTime = LocalDateTime.parse(departureTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime arrivalDataTime = LocalDateTime.parse(arrivalTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        List<Flight> directsFlights = directFlightProvider.getFlights(departure, arrival, departureDataTime, arrivalDataTime);
        List<Flight> indirectFlights = indirectFlightProvider.getFlights(departure, arrival, departureDataTime, arrivalDataTime);
        return Stream.concat(directsFlights.stream(), indirectFlights.stream()).collect(Collectors.toList());
    }
}
