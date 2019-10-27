package com.ryanair.flight.provider;

import com.ryanair.flight.client.SchedulesApiClient;
import com.ryanair.flight.domain.Flight;
import com.ryanair.flight.domain.IndirectRout;
import com.ryanair.flight.domain.Leg;
import com.ryanair.flight.domain.Rout;
import com.ryanair.flight.domain.Schedule;
import com.ryanair.flight.domain.ScheduleDay;
import com.ryanair.flight.domain.ScheduledFlight;
import com.ryanair.flight.provider.IndirectFlightProvider;
import com.ryanair.flight.provider.IndirectRoutsProvider;
import com.ryanair.flight.provider.RoutsProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IndirectFlightProviderTest {
    private static String DEPARTURE = "DUB";
    private static String ARRIVAL = "WROC";
    private static String STOP = "STN";
    private LocalDateTime DEPARTURE_DATA_TIME = LocalDateTime.parse("2018-03-02T07:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    private LocalDateTime ARRIVAL_DATA_TIME = LocalDateTime.parse("2018-03-04T21:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    private static final int DAY_OF_MONTH = 2;
    private static final int MONTH_VALUE = 3;
    private static final String DEPARTURE_TIME_FOR_FIRST_LEG = "09:00";
    private static final String ARRIVAL_TIME_FOR_FIRST_LEG = "12:35";

    private static final String DEPARTURE_TIME_FOR_SECOND_LEG = "13:00";
    private static final String ARRIVAL_TIME_FOR_SECOND_LEG = "15:35";

    private static final String DEPARTURE_TIME_FOR_SECOND_LEG_GREATER_THEN_TWO_H = "17:00";
    private static final String ARRIVAL_TIME_FOR_SECOND_LEG_GREATER_THEN_TWO_H = "21:35";

    @Mock
    private RoutsProvider routsProvider;

    @Mock
    private IndirectRoutsProvider indirectRoutsProvider;

    @Mock
    private SchedulesApiClient schedulesApiClient;

    @InjectMocks
    IndirectFlightProvider indirectFlightProvider;

    @Test
    public void shouldReturnListOfFlightsWhenTimeDifferencesBetweenLegsIsSmallerThenTwoH() {
        Rout firstLegRout = Rout.builder()
                .airportFrom(DEPARTURE)
                .airportTo(STOP)
                .build();

        Rout secondLegRout = Rout.builder()
                .airportFrom(STOP)
                .airportTo(ARRIVAL)
                .build();

        List<Rout> listOfRouts = List.of(firstLegRout, secondLegRout);
        when(routsProvider.getRouts(DEPARTURE, ARRIVAL)).thenReturn(listOfRouts);

        List<IndirectRout> indirectRoutsList = List.of(IndirectRout.builder().firstLeg(firstLegRout).secondLeg(secondLegRout).build());
        when(indirectRoutsProvider.matchRouts(DEPARTURE, ARRIVAL, listOfRouts)).thenReturn(indirectRoutsList);

        when(schedulesApiClient.getSchedule(DEPARTURE, STOP, DEPARTURE_DATA_TIME.getYear(), DEPARTURE_DATA_TIME.getMonthValue()))
            .thenReturn(Schedule.builder()
                .month(MONTH_VALUE)
                .days(List.of(ScheduleDay.builder()
                        .day(DAY_OF_MONTH)
                        .flights(List.of(ScheduledFlight.builder()
                                .departureTime(DEPARTURE_TIME_FOR_FIRST_LEG)
                                .arrivalTime(ARRIVAL_TIME_FOR_FIRST_LEG)
                                .build()))
                        .build()))
                .build());

        when(schedulesApiClient.getSchedule(STOP, ARRIVAL, DEPARTURE_DATA_TIME.getYear(), DEPARTURE_DATA_TIME.getMonthValue()))
                .thenReturn(Schedule.builder()
                        .month(MONTH_VALUE)
                        .days(List.of(ScheduleDay.builder()
                                .day(DAY_OF_MONTH)
                                .flights(List.of(ScheduledFlight.builder()
                                        .departureTime(DEPARTURE_TIME_FOR_SECOND_LEG)
                                        .arrivalTime(ARRIVAL_TIME_FOR_SECOND_LEG)
                                        .build()))
                                .build()))
                        .build());

        Leg firstLeg = Leg.builder()
                .departureAirport(DEPARTURE)
                .departureDateTime(LocalDateTime.parse("2018-03-02T09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .arrivalAirport(STOP)
                .arrivalDateTime(LocalDateTime.parse("2018-03-02T12:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Leg secondLeg = Leg.builder()
                .departureAirport(STOP)
                .departureDateTime(LocalDateTime.parse("2018-03-02T13:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .arrivalAirport(ARRIVAL)
                .arrivalDateTime(LocalDateTime.parse("2018-03-02T15:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        List<Flight> flightsList = indirectFlightProvider.getFlights(DEPARTURE, ARRIVAL, DEPARTURE_DATA_TIME, ARRIVAL_DATA_TIME);

        Assertions.assertThat(flightsList).containsExactlyInAnyOrder(Flight.builder().stops(2).legs(List.of(firstLeg, secondLeg)).build());
    }

    @Test
    public void shouldReturnEmptyListOfFlightsWhenTimeDifferencesBetweenLegsIsSmallerThenTwoH() {
        Rout firstLegRout = Rout.builder()
                .airportFrom(DEPARTURE)
                .airportTo(STOP)
                .build();

        Rout secondLegRout = Rout.builder()
                .airportFrom(STOP)
                .airportTo(ARRIVAL)
                .build();

        List<Rout> listOfRouts = List.of(firstLegRout, secondLegRout);
        when(routsProvider.getRouts(DEPARTURE, ARRIVAL)).thenReturn(listOfRouts);

        List<IndirectRout> indirectRoutsList = List.of(IndirectRout.builder().firstLeg(firstLegRout).secondLeg(secondLegRout).build());
        when(indirectRoutsProvider.matchRouts(DEPARTURE, ARRIVAL, listOfRouts)).thenReturn(indirectRoutsList);

        when(schedulesApiClient.getSchedule(DEPARTURE, STOP, DEPARTURE_DATA_TIME.getYear(), DEPARTURE_DATA_TIME.getMonthValue()))
                .thenReturn(Schedule.builder()
                        .month(MONTH_VALUE)
                        .days(List.of(ScheduleDay.builder()
                                .day(DAY_OF_MONTH)
                                .flights(List.of(ScheduledFlight.builder()
                                        .departureTime(DEPARTURE_TIME_FOR_FIRST_LEG)
                                        .arrivalTime(ARRIVAL_TIME_FOR_FIRST_LEG)
                                        .build()))
                                .build()))
                        .build());

        when(schedulesApiClient.getSchedule(STOP, ARRIVAL, DEPARTURE_DATA_TIME.getYear(), DEPARTURE_DATA_TIME.getMonthValue()))
                .thenReturn(Schedule.builder()
                        .month(MONTH_VALUE)
                        .days(List.of(ScheduleDay.builder()
                                .day(DAY_OF_MONTH)
                                .flights(List.of(ScheduledFlight.builder()
                                        .departureTime(DEPARTURE_TIME_FOR_SECOND_LEG_GREATER_THEN_TWO_H)
                                        .arrivalTime(ARRIVAL_TIME_FOR_SECOND_LEG_GREATER_THEN_TWO_H)
                                        .build()))
                                .build()))
                        .build());

        List<Flight> flightsList = indirectFlightProvider.getFlights(DEPARTURE, ARRIVAL, DEPARTURE_DATA_TIME, ARRIVAL_DATA_TIME);

        Assertions.assertThat(flightsList.isEmpty()).isTrue();
    }
}