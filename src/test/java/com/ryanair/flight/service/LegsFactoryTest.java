package com.ryanair.flight.service;

import com.ryanair.flight.domain.Leg;
import com.ryanair.flight.domain.Schedule;
import com.ryanair.flight.domain.ScheduleDay;
import com.ryanair.flight.domain.ScheduledFlight;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class LegsFactoryTest {
    private final static String DEPARTURE = "DUB";
    private final static String ARRIVAL = "WROC";
    private final static int YEAR = 2018;

    LegsFactory factory = new LegsFactory(DEPARTURE, ARRIVAL, YEAR);

    @Test
    public void testThree() throws Exception{
        ScheduledFlight firstScheduledFlight = ScheduledFlight.builder().departureTime("09:00").arrivalTime("12:35").build();
        ScheduledFlight secondScheduledFlight = ScheduledFlight.builder().departureTime("18:00").arrivalTime("21:35").build();

        ScheduleDay firstScheduleDay = ScheduleDay.builder()
                .day(1)
                .flights(List.of(firstScheduledFlight,secondScheduledFlight))
                .build();

        ScheduleDay secondScheduleDay = ScheduleDay.builder()
                .day(2)
                .flights(List.of(firstScheduledFlight,secondScheduledFlight))
                .build();

        Schedule schedule = Schedule.builder().month(3).days(List.of(firstScheduleDay, secondScheduleDay)).build();

        Leg firstLeg = Leg.builder()
                .departureAirport(DEPARTURE)
                .departureDateTime(LocalDateTime.parse("2018-03-01T09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .arrivalAirport(ARRIVAL)
                .arrivalDateTime(LocalDateTime.parse("2018-03-01T12:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Leg secondLeg = Leg.builder()
                .departureAirport(DEPARTURE)
                .departureDateTime(LocalDateTime.parse("2018-03-01T18:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .arrivalAirport(ARRIVAL)
                .arrivalDateTime(LocalDateTime.parse("2018-03-01T21:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Leg thirdLeg = Leg.builder()
                .departureAirport(DEPARTURE)
                .departureDateTime(LocalDateTime.parse("2018-03-01T09:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .arrivalAirport(ARRIVAL)
                .arrivalDateTime(LocalDateTime.parse("2018-03-01T12:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        Leg forthLeg = Leg.builder()
                .departureAirport(DEPARTURE)
                .departureDateTime(LocalDateTime.parse("2018-03-02T18:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .arrivalAirport(ARRIVAL)
                .arrivalDateTime(LocalDateTime.parse("2018-03-02T21:35", DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();

        List<Leg> actualLeg = factory.create(schedule);
        Assertions.assertThat(actualLeg.size()).isEqualTo(4);
        Assertions.assertThat(actualLeg).contains(firstLeg);
        Assertions.assertThat(actualLeg).contains(secondLeg);
        Assertions.assertThat(actualLeg).contains(thirdLeg);
        Assertions.assertThat(actualLeg).contains(forthLeg);

    }
}