package com.ryanair.flight.provider;

import com.ryanair.flight.domain.IndirectRout;
import com.ryanair.flight.domain.Rout;
import com.ryanair.flight.provider.IndirectRoutsProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class IndirectRoutsProviderTest {

    private IndirectRoutsProvider provider = new IndirectRoutsProvider();

    @Test
    public void shouldReturnListOfIndirectRoutWhenThereIsOnlyOnePairOfRoutsWhichPass() {
        Rout firstLegRout = Rout.builder()
                .airportFrom("DUB")
                .airportTo("STN")
                .build();

        Rout secondLegRout = Rout.builder()
                .airportFrom("STN")
                .airportTo("WROC")
                .build();

        List<Rout> listOfAllRouts = List.of(firstLegRout, secondLegRout);

        List<IndirectRout> listOfIndirectRouts = provider.matchRouts("DUB", "WROC", listOfAllRouts);
        Assertions.assertThat(listOfIndirectRouts).containsExactlyInAnyOrder(
                IndirectRout.builder().firstLeg(firstLegRout).secondLeg(secondLegRout).build());
    }

    @Test
    public void shouldReturnEmptyListOfIndirectRoutWhenThereIsNoPairOfRoutsWhichPass() {
        Rout firstLegRout = Rout.builder()
                .airportFrom("DUB")
                .airportTo("CHQ")
                .build();

        Rout secondLegRout = Rout.builder()
                .airportFrom("SKG")
                .airportTo("WROC")
                .build();

        List<Rout> listOfAllRouts = List.of(firstLegRout, secondLegRout);

        List<IndirectRout> listOfIndirectRouts = provider.matchRouts("DUB", "WROC", listOfAllRouts);
        Assertions.assertThat(listOfIndirectRouts.isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnListOfIndirectRoutWhenThereIsOnPairOfRoutsWhichPassAndOneWhichNotPass() {
        Rout firstPassingLegRout = Rout.builder()
                .airportFrom("DUB")
                .airportTo("STN")
                .build();

        Rout secondPassingLegRout = Rout.builder()
                .airportFrom("STN")
                .airportTo("WROC")
                .build();

        Rout firstNonPassingLegRout = Rout.builder()
                .airportFrom("DUB")
                .airportTo("CHQ")
                .build();

        Rout secondNonPassingLegRout = Rout.builder()
                .airportFrom("SKG")
                .airportTo("WROC")
                .build();

        List<IndirectRout> listOfIndirectRouts = provider.matchRouts("DUB", "WROC",
                List.of(firstPassingLegRout, firstNonPassingLegRout, secondPassingLegRout, secondNonPassingLegRout));
        Assertions.assertThat(listOfIndirectRouts).containsExactlyInAnyOrder(
                IndirectRout.builder().firstLeg(firstPassingLegRout).secondLeg(secondPassingLegRout).build());
    }

    @Test
    public void shouldReturnListOfIndirectRoutWhenThereIsOnlyOnePairOfRoutsWhichPassAnOneDirectFlightRout() {
        Rout directRout = Rout.builder()
                .airportFrom("DUB")
                .airportTo("WROC")
                .build();

        Rout firstLegRout = Rout.builder()
                .airportFrom("DUB")
                .airportTo("STN")
                .build();

        Rout secondLegRout = Rout.builder()
                .airportFrom("STN")
                .airportTo("WROC")
                .build();

        List<Rout> listOfAllRouts = List.of(firstLegRout, secondLegRout, directRout);

        List<IndirectRout> listOfIndirectRouts = provider.matchRouts("DUB", "WROC", listOfAllRouts);
        Assertions.assertThat(listOfIndirectRouts).containsExactlyInAnyOrder(
                IndirectRout.builder().firstLeg(firstLegRout).secondLeg(secondLegRout).build());
    }
 }