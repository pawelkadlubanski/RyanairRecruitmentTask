package com.ryanair.flight.provider;

import com.ryanair.flight.client.SchedulesApiClient;
import com.ryanair.flight.domain.Flight;
import com.ryanair.flight.domain.Leg;
import com.ryanair.flight.domain.Schedule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DirectFlightProvider implements FlightProvider{
    private static final int NUMBER_OF_STEPS_FOR_DIRECT_FLIGHT = 0;

    private final SchedulesApiClient schedulesApiClient;

    @Override
    public List<Flight> getFlights(String departure, String arrival, LocalDateTime departureDataTime, LocalDateTime arrivalDataTime) {
        int year = departureDataTime.getYear();
        Schedule schedule = schedulesApiClient.getSchedule(departure, arrival, year, departureDataTime.getMonthValue());
        return getLegs(departure, arrival, departureDataTime, schedule).stream()
                .filter(leg -> isLegInRage(departureDataTime, arrivalDataTime, leg))
                .map(leg -> createFlight(leg))
                .collect(Collectors.toList());
    }

    private List<Leg> getLegs(String departure, String arrival, LocalDateTime departureDataTime, Schedule schedule) {
        return new LegsFactory(departure, arrival, departureDataTime.getYear()).create(schedule);
    }

    private Flight createFlight(Leg leg) {
        return Flight.builder()
            .stops(NUMBER_OF_STEPS_FOR_DIRECT_FLIGHT)
            .legs(List.of(leg))
            .build();
    }

    private boolean isLegInRage(LocalDateTime departureDataTime, LocalDateTime arrivalDataTime, Leg leg) {
        return leg.getDepartureDateTime().isAfter(departureDataTime)
                && leg.getArrivalDateTime().isBefore(arrivalDataTime);
    }
}
