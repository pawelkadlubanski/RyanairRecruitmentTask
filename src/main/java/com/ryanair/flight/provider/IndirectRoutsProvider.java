package com.ryanair.flight.provider;

import com.ryanair.flight.domain.IndirectRout;
import com.ryanair.flight.domain.Rout;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class IndirectRoutsProvider {

    private Optional<IndirectRout> matchSecondLeg(Rout firstLegRout, List<Rout> secondLegRoutsList) {
        String firstLegArrival = firstLegRout.getAirportTo();
        return secondLegRoutsList.stream()
                .filter(rout -> rout.getAirportFrom().equals(firstLegArrival))
                .findFirst()
                .map(rout -> IndirectRout.builder().firstLeg(firstLegRout).secondLeg(rout).build());
    }

    public List<IndirectRout> matchRouts(String departure, String arrival, List<Rout> listOfAllRouts) {
        List<Rout> firstLegRoutsList = listOfAllRouts.stream()
                .filter(rout -> rout.getAirportFrom().equals(departure))
                .collect(Collectors.toList());

        List<Rout> secondLegRoutsList = listOfAllRouts.stream()
                .filter(rout -> rout.getAirportTo().equals(arrival))
                .collect(Collectors.toList());

        return firstLegRoutsList.stream()
                .map(rout -> matchSecondLeg(rout, secondLegRoutsList))
                .filter(indirectRout -> indirectRout.isPresent())
                .map(indirectRout -> indirectRout.get())
                .collect(Collectors.toList());
    }
}
