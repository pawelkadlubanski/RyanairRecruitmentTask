package com.ryanair.flight.provider;

import com.ryanair.flight.client.RoutsApiClient;
import com.ryanair.flight.domain.Rout;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class RoutsProvider {
    private static final String OPERATOR = "RYANAIR";

    private final RoutsApiClient routsApiClient;

    public List<Rout> getRouts(String departure, String arrival) {
        return routsApiClient.getRouts()
            .stream()
            .filter(rout -> isRoutCorrect(departure, arrival, rout))
            .collect(Collectors.toList());
    }

    private boolean isRoutCorrect(String departure, String arrival, Rout rout) {
        return isConnectingAirPortCorrect(rout)
                && isOperatorCorrect(rout)
                && (rout.getAirportFrom().equals(departure)
                || rout.getAirportTo().equals(arrival));
    }

    private boolean isConnectingAirPortCorrect(Rout rout) {
        return Objects.isNull(rout.getConnectingAirport());
    }

    private boolean isOperatorCorrect(Rout rout) {
        return Objects.nonNull(rout.getOperator())
                && rout.getOperator().equals(OPERATOR);
    }
}

