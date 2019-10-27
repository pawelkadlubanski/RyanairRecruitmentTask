package com.ryanair.flight.controller;

import com.ryanair.flight.domain.Flight;
import com.ryanair.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(
    path = "/scheduledFlights",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class FlightsServiceController {
    private final FlightService service;

    @GetMapping(path = "/interconnections")
    public ResponseEntity<List<Flight>> getFlight(@RequestParam(name = "departure") String departure,
                                                  @RequestParam(name = "arrival") String arrival,
                                                  @RequestParam(name = "departureDateTime") String departureDateTime,
                                                  @RequestParam(name = "arrivalDateTime") String arrivalDateTime) {

        return ResponseEntity.ok(service.getFlights(departure, arrival, departureDateTime, arrivalDateTime));
    }


}
