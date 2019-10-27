package com.ryanair.flight.service;

import com.ryanair.flight.domain.Flight;
import com.ryanair.flight.domain.Leg;
import com.ryanair.flight.provider.DirectFlightProvider;
import com.ryanair.flight.provider.IndirectFlightProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    private static final LocalDateTime DEPARTURE_DATE_TIME = LocalDateTime.parse("2018-03-02T09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    public static final LocalDateTime ARRIVAL_DATE_TIME = LocalDateTime.parse("2018-03-02T12:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    private static String DEPARTURE = "DUB";
    private static String ARRIVAL = "WROC";
    private static String DEPARTURE_TIME = "2018-03-02T07:00";
    private static String ARRIVAL_TIME = "2018-03-04T21:00";

    @Mock
    private DirectFlightProvider directFlightProvider;

    @Mock
    private IndirectFlightProvider indirectFlightProvider;

    @InjectMocks
    private FlightService service;

    @Test
    public void shouldReturnConcatenationOfDirectAnIndirectFlights() {
        Flight directFlight = Flight.builder().stops(0).legs(Collections.emptyList()).build();
        Flight indirectFlight = Flight.builder().stops(0).legs(Collections.emptyList()).build();
        Mockito.when(directFlightProvider.getFlights(DEPARTURE, ARRIVAL, DEPARTURE_DATE_TIME, ARRIVAL_DATE_TIME))
            .thenReturn(List.of(directFlight));
        Mockito.when(indirectFlightProvider.getFlights(DEPARTURE, ARRIVAL, DEPARTURE_DATE_TIME, ARRIVAL_DATE_TIME))
            .thenReturn(List.of(indirectFlight));
        List<Flight> flights = service.getFlights(DEPARTURE, ARRIVAL, DEPARTURE_TIME, ARRIVAL_TIME);
        Assertions.assertThat(flights).containsExactlyInAnyOrder(directFlight, indirectFlight);
    }
}