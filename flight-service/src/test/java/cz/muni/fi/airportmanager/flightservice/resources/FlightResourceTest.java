package cz.muni.fi.airportmanager.flightservice.resources;


import cz.muni.fi.airportmanager.flightservice.model.CreateFlightDto;
import cz.muni.fi.airportmanager.flightservice.model.FlightDto;
import cz.muni.fi.airportmanager.flightservice.model.FlightStatus;
import cz.muni.fi.airportmanager.flightservice.service.FlightService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


@QuarkusTest
@TestHTTPEndpoint(FlightResource.class) // this tells Quarkus that requests will begin with /flight
class FlightResourceTest {

    @InjectMock
    FlightService flightService;

    @Test
    void shouldGetEmptyListOfFlights() {
        Mockito.when(this.flightService.listAll()).thenReturn(Uni.createFrom().item(List.of()));

        given().when()
                .get()
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void shouldGetListOfFlights() {
        Mockito.when(this.flightService.listAll()).thenReturn(Uni.createFrom().item(List.of(getFlightDto())));

        given().when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void shouldCreateFlight() {
        FlightDto responseFlight = getFlightDto();
        Mockito.when(this.flightService.createFlight(Mockito.any(CreateFlightDto.class))).thenReturn(Uni.createFrom().item(responseFlight));

        CreateFlightDto testFlight = responseFlight;

        given().contentType("application/json")
                .body(testFlight)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", equalTo((int) responseFlight.id));
    }

    @Test
    void shouldGetExistingFlight() {
        FlightDto testFlight = getFlightDto();
        Mockito.when(this.flightService.getFlight(testFlight.id)).thenReturn(Uni.createFrom().item(testFlight));

        given().when()
                .get("/" + testFlight.id)
                .then()
                .statusCode(200)
                .body("id", equalTo((int) testFlight.id));
    }

    @Test
    void shouldNotGetNonxistingFlight() {
        Mockito.when(this.flightService.getFlight(Mockito.anyLong())).thenReturn(Uni.createFrom().failure(new IllegalArgumentException()));

        given().when()
                .get("/99") // Assuming ID 99 does not exist
                .then()
                .statusCode(404);
    }

    @Test
    void shouldDeleteExistingFlight() {
        FlightDto testFlight = getFlightDto();
        Mockito.when(this.flightService.deleteFlight(testFlight.id)).thenReturn(Uni.createFrom().item(true));


        given().when()
                .delete("/" + testFlight.id)
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotDeleteNonexistingFlight() {
        Mockito.when(this.flightService.deleteFlight(Mockito.anyLong())).thenReturn(Uni.createFrom().item(false));

        given().when()
                .delete("/99") // Assuming ID 99 does not exist
                .then()
                .statusCode(404);
    }


    @Test
    void shouldNotCancelNonexistingFlight() {
        given().when()
                .put("/99/cancel") // Assuming ID 99 does not exist
                .then()
                .statusCode(404);
    }


    private FlightDto getFlightDto() {
        FlightDto flight = new FlightDto();
        flight.id = 1L;
        flight.name = "Test Flight";
        flight.airportTo = "Airport A";
        flight.airportFrom = "Airport B";
        flight.departureTime = new Date();
        flight.arrivalTime = new Date();
        flight.capacity = 100;
        flight.status = FlightStatus.ACTIVE;
        return flight;
    }
}