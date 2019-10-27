package com.ryanair.flight.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.ryanair.flight.client.SchedulesApiClient;
import com.ryanair.flight.config.RoutsApiClientConfig;
import com.ryanair.flight.domain.Flight;
import com.ryanair.flight.service.FlightService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {RoutsApiClientConfig.class, SchedulesApiClient.class})
class FlightsServiceControllerTest {
    private static final String DEPARTURE = "DUB";
    private static final String ARRIVAL = "WROC";
    private static final String DEPARTURE_DATA_TIME = "2018-03-01T07:00";
    private static final String ARRIVAL_DATA_TIME="2018-03-03T21:00";

    private static final String FLIGHTS = "/scheduledFlights";

    @MockBean
    private FlightService flightService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = FLIGHTS;
    }

    @Test
    public void shouldReturnListOfLights() {
        Flight flight = Flight.builder().build();
        Mockito.when(flightService.getFlights(DEPARTURE, ARRIVAL, DEPARTURE_DATA_TIME, ARRIVAL_DATA_TIME))
                .thenReturn(List.of(flight));

        String requestUrl = "interconnections?departure=DUB&arrival=WRO&departureDateTime=2018-03-01T07:00&arrivalDateTime=2018-03-03T21:00";
        List<Flight> flightList = Arrays.asList(given()
                .when()
                .contentType(ContentType.JSON)
                .get(requestUrl)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .as(Flight[].class));
        Assertions.assertThat(flightList).containsExactlyInAnyOrder(flight);
    }
}