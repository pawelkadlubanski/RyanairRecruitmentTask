package com.ryanair.flight.provider;

import com.ryanair.flight.client.SchedulesApiClient;
import com.ryanair.flight.domain.Flight;
import com.ryanair.flight.domain.Leg;
import com.ryanair.flight.domain.Schedule;
import com.ryanair.flight.domain.ScheduleDay;
import com.ryanair.flight.domain.ScheduledFlight;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DirectFlightProviderTest {
    private static final String DEPARTURE = "DUB";
    private static final String ARRIVAL = "WROC";
    private static final int DAY_OF_MONTH_BEFORE_DEPARTURE = 1;
    private static final int DAY_OF_MONTH_IN_RANGE = 2;
    private static final int DAY_OF_MONTH_AFTER_ARRIVAL = 5;
    private static final int MONTH_VALUE = 3;
    private static final String DEPARTURE_TIME = "09:00";
    private static final String ARRIVAL_TIME = "12:35";
    private LocalDateTime DEPARTURE_DATA_TIME = LocalDateTime.parse("2018-03-02T07:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    private LocalDateTime ARRIVAL_DATA_TIME = LocalDateTime.parse("2018-03-04T21:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    @Mock
    private SchedulesApiClient schedulesApiClient;

    @InjectMocks
    private DirectFlightProvider provider;

    @Test
    public void shouldReturnCorrectListOfFlightsWhenAllFlightsInRange() {
        ScheduledFlight firstScheduledFlight = ScheduledFlight.builder()
                .departureTime(DEPARTURE_TIME)
                .arrivalTime(ARRIVAL_TIME)
                .build();

        ScheduleDay scheduleDayInRange = ScheduleDay.builder()
                .day(DAY_OF_MONTH_IN_RANGE)
                .flights(List.of(firstScheduledFlight))
                .build();

        Schedule schedule = Schedule.builder()
                .month(MONTH_VALUE)
                .days(List.of(scheduleDayInRange))
                .build();

        when(schedulesApiClient.getSchedule(DEPARTURE,
                ARRIVAL,
                DEPARTURE_DATA_TIME.getYear(),
                DEPARTURE_DATA_TIME.getMonthValue()))
            .thenReturn(schedule);

        Leg leg = Leg.builder()
            .departureAirport(DEPARTURE)
            .departureDateTime(LocalDateTime.parse("2018-03-02T09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .arrivalAirport(ARRIVAL)
            .arrivalDateTime(LocalDateTime.parse("2018-03-02T12:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .build();

        Flight flight = Flight.builder().stops(0).legs(List.of(leg)).build();

        List<Flight> flightList = provider.getFlights(DEPARTURE, ARRIVAL, DEPARTURE_DATA_TIME, ARRIVAL_DATA_TIME);

        Assertions.assertThat(flightList).containsOnly(flight);
    }

    @Test
    public void shouldReturnCorrectListOfFlightsWhenAllOnlyOneFlightsInRange() {
        ScheduledFlight firstScheduledFlight = ScheduledFlight.builder()
                .departureTime(DEPARTURE_TIME)
                .arrivalTime(ARRIVAL_TIME)
                .build();

        ScheduleDay scheduleDayBeforeDeparture = ScheduleDay.builder()
                .day(DAY_OF_MONTH_BEFORE_DEPARTURE)
                .flights(List.of(firstScheduledFlight))
                .build();

        ScheduleDay scheduleDayInRange = ScheduleDay.builder()
                .day(DAY_OF_MONTH_IN_RANGE)
                .flights(List.of(firstScheduledFlight))
                .build();

        ScheduleDay scheduleDayAfterArrival = ScheduleDay.builder()
                .day(DAY_OF_MONTH_AFTER_ARRIVAL)
                .flights(List.of(firstScheduledFlight))
                .build();

        Schedule schedule = Schedule.builder()
                .month(MONTH_VALUE)
                .days(List.of(
                        scheduleDayBeforeDeparture,
                        scheduleDayInRange,
                        scheduleDayAfterArrival))
                .build();

        when(schedulesApiClient.getSchedule(DEPARTURE,
                ARRIVAL,
                DEPARTURE_DATA_TIME.getYear(),
                DEPARTURE_DATA_TIME.getMonthValue()))
                .thenReturn(schedule);

        Leg leg = Leg.builder()
                .departureAirport(DEPARTURE)
                .departureDateTime(LocalDateTime.parse("2018-03-02T09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .arrivalAirport(ARRIVAL)
                .arrivalDateTime(LocalDateTime.parse("2018-03-02T12:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Flight flight = Flight.builder().stops(0).legs(List.of(leg)).build();

        List<Flight> flightList = provider.getFlights(DEPARTURE, ARRIVAL, DEPARTURE_DATA_TIME, ARRIVAL_DATA_TIME);

        Assertions.assertThat(flightList).containsOnly(flight);
    }
}