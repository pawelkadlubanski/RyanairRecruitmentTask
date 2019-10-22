package com.ryanair.flight.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.jayway.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlightsServiceControllerTest {
    public static final String FLIGHTS = "/flights";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = FLIGHTS;
    }

    @Test
    public void dummyTest() {
        String response = given()
                .when()
                .contentType(ContentType.JSON)
                .post("/test")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .asString();
        Assertions.assertThat(response).isEqualToIgnoringCase("I am Ok");
    }
}