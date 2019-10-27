package com.ryanair.flight.provider;

import com.ryanair.flight.client.SchedulesApiClient;
import com.ryanair.flight.domain.Flight;
import com.ryanair.flight.domain.IndirectRout;
import com.ryanair.flight.domain.Leg;
import com.ryanair.flight.domain.Rout;
import com.ryanair.flight.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class IndirectFlightProvider implements FlightProvider {
    private static final int MAX_DURATION_BETWEEN_LEGS = 2;

    private RoutsProvider routsProvider;
    private IndirectRoutsProvider indirectRoutsProvider;
    private SchedulesApiClient schedulesApiClient;

    @Override
    public List<Flight> getFlights(String departure, String arrival, LocalDateTime departureDataTime, LocalDateTime arrivalDataTime) {
        List<Rout> availableRouts = routsProvider.getRouts(departure, arrival);
        List<IndirectRout> indirectRouts = indirectRoutsProvider.matchRouts(departure, arrival, availableRouts);
        return indirectRouts.stream().map(rout -> {
                List<Leg> firstLegsList = getLegsList(rout.getFirstLeg(), departureDataTime);
                List<Leg> secondLegsList = getLegsList(rout.getSecondLeg(), departureDataTime);
                return getFlightList(firstLegsList, secondLegsList);})
            .flatMap(list -> list.stream())
            .collect(Collectors.toList());
    }

    private List<Leg> getLegsList(Rout rout, LocalDateTime departureDataTime) {
        final LegsFactory legsFactory = new LegsFactory(rout.getAirportFrom(), rout.getAirportTo(), departureDataTime.getYear());
        Schedule scheduleForFirstLeg = schedulesApiClient
                .getSchedule(rout.getAirportFrom(),
                        rout.getAirportTo(),
                        departureDataTime.getYear(),
                        departureDataTime.getMonthValue());
        return legsFactory.create(scheduleForFirstLeg);
    }

    private List<Flight> matchSecondLeg(Leg firstLeg, List<Leg> secondLeg) {
        LocalDateTime firstLegsArrivalTime = firstLeg.getArrivalDateTime();
        return secondLeg.stream()
            .filter(leg -> Duration.between(firstLegsArrivalTime, leg.getDepartureDateTime()).toHours() <= MAX_DURATION_BETWEEN_LEGS)
            .map(leg -> Flight.builder().stops(1).legs(List.of(firstLeg, leg)).build())
            .collect(Collectors.toList());
    }

    private List<Flight> getFlightList(List<Leg> firstLegsList, List<Leg> secondLegsList) {
        return firstLegsList.stream()
                .map(firstLeg -> matchSecondLeg(firstLeg, secondLegsList))
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());
    }

}
