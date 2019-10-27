package com.ryanair.flight.provider;

import com.ryanair.flight.domain.Leg;
import com.ryanair.flight.domain.Schedule;
import com.ryanair.flight.domain.ScheduleDay;
import com.ryanair.flight.domain.ScheduledFlight;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LegsFactory {
    private final String departure;
    private final String arrival;
    private int year;

    public List<Leg> create(Schedule scheduled) {
        Integer month = scheduled.getMonth();
        return scheduled.getDays().stream()
            .map(day -> createListOfLegFrom(month, day))
            .flatMap(list -> list.stream())
            .collect(Collectors.toList());
    }

    private List<Leg> createListOfLegFrom(int month, ScheduleDay scheduledDay) {
        Integer day = scheduledDay.getDay();
        return scheduledDay.getFlights().stream()
                .map(scheduledFlight -> createLegFrom(month, day, scheduledFlight))
                .collect(Collectors.toList());
    }

    private Leg createLegFrom(int month, int dayOfMonth, ScheduledFlight scheduledFlight) {
        return Leg.builder()
                .departureAirport(departure)
                .departureDateTime(createTimeFrom(year, month, dayOfMonth, scheduledFlight.getDepartureTime()))
                .arrivalAirport(arrival)
                .arrivalDateTime(createTimeFrom(year, month, dayOfMonth, scheduledFlight.getArrivalTime()))
                .build();
    }

    private LocalDateTime createTimeFrom(int year, int month, int day, String timeAsString) {
        LocalTime localTime = LocalTime.parse(timeAsString, DateTimeFormatter.ofPattern("HH:mm"));
        return LocalDateTime.of(year,month, day, localTime.getHour(),localTime.getMinute());
    }
}
