package com.ryanair.flight.provider;

import com.ryanair.flight.client.RoutsApiClient;
import com.ryanair.flight.domain.Rout;
import com.ryanair.flight.provider.RoutsProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutsProviderTest {
    private static final String INCORRECT_OPERATOR = "NOT_RYANAIR";
    private static final String CORRECT_OPERATOR = "RYANAIR";
    private static final String CONNECTING_AIRPORT = "CONNECTING_AIRPORT";
    private static final String CORRECT_DEPARTURE = "DUB";
    private static final String CORRECT_ARRIVAL = "WROC";
    private static final String INCORRECT_DEPARTURE = "LUZ";
    private static final String INCORRECT_ARRIVAL = "STN";
    private static final Rout CORRECT_ROUT = Rout.builder()
            .operator(CORRECT_OPERATOR)
            .airportFrom(CORRECT_DEPARTURE)
            .airportTo(CORRECT_ARRIVAL)
            .build();

    @Mock
    private RoutsApiClient routsApiClient;

    @InjectMocks
    private RoutsProvider provider;

    @Test
    public void shouldReturnOnlyRoutsWhichConnectingAirPortIsNull() {
        Rout incorrectRout = Rout.builder().connectingAirport(CONNECTING_AIRPORT).build();

        when(routsApiClient.getRouts()).thenReturn(List.of(CORRECT_ROUT, incorrectRout));

        List<Rout> routsList = provider.getRouts(CORRECT_DEPARTURE, CORRECT_ARRIVAL);

        Assertions.assertThat(routsList).containsOnly(CORRECT_ROUT);
   }

    @Test
    public void shouldReturnOnlyRoutsWhichOperatorIsRyanair() {
        Rout incorrectRout = Rout.builder()
                .operator(INCORRECT_OPERATOR)
                .build();

        when(routsApiClient.getRouts()).thenReturn(List.of(CORRECT_ROUT, incorrectRout));

        List<Rout> routsList = provider.getRouts(CORRECT_DEPARTURE, CORRECT_ARRIVAL);

        Assertions.assertThat(routsList).containsOnly(CORRECT_ROUT);
    }

    @Test
    public void shouldReturnOnlyRoutsWhichDepartureAndArrivalIsCorrect() {
        Rout incorrectRout = Rout.builder()
                .operator(CORRECT_OPERATOR)
                .airportFrom(INCORRECT_DEPARTURE)
                .airportTo(INCORRECT_ARRIVAL)
                .build();

        when(routsApiClient.getRouts()).thenReturn(List.of(CORRECT_ROUT, incorrectRout));

        List<Rout> routsList = provider.getRouts(CORRECT_DEPARTURE, CORRECT_ARRIVAL);

        Assertions.assertThat(routsList).containsOnly(CORRECT_ROUT);
    }

    @Test
    public void shouldReturnRoutsWhichDepartureIsCorrect() {
        Rout incorrectRout = Rout.builder()
                .operator(CORRECT_OPERATOR)
                .airportFrom(INCORRECT_DEPARTURE)
                .airportTo(INCORRECT_ARRIVAL)
                .build();

        Rout routWithCorrectDeparture = Rout.builder()
                .operator(CORRECT_OPERATOR)
                .airportFrom(CORRECT_DEPARTURE)
                .airportTo(INCORRECT_ARRIVAL)
                .build();

        when(routsApiClient.getRouts()).thenReturn(List.of(CORRECT_ROUT, incorrectRout, routWithCorrectDeparture));

        List<Rout> routsList = provider.getRouts(CORRECT_DEPARTURE, CORRECT_ARRIVAL);

        Assertions.assertThat(routsList).containsExactlyInAnyOrder(CORRECT_ROUT, routWithCorrectDeparture);
    }

    @Test
    public void shouldReturnRoutsWhichArrivalIsCorrect() {
        Rout incorrectRout = Rout.builder()
                .operator(CORRECT_OPERATOR)
                .airportFrom(INCORRECT_DEPARTURE)
                .airportTo(INCORRECT_ARRIVAL)
                .build();

        Rout routWithCorrectArrival = Rout.builder()
                .operator(CORRECT_OPERATOR)
                .airportFrom(INCORRECT_DEPARTURE)
                .airportTo(CORRECT_ARRIVAL)
                .build();

        when(routsApiClient.getRouts()).thenReturn(List.of(CORRECT_ROUT, incorrectRout, routWithCorrectArrival));

        List<Rout> routsList = provider.getRouts(CORRECT_DEPARTURE, CORRECT_ARRIVAL);

        Assertions.assertThat(routsList).containsExactlyInAnyOrder(CORRECT_ROUT, routWithCorrectArrival);
    }
}