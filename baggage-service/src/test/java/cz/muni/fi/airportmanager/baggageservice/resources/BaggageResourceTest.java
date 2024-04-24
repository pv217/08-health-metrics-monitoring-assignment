package cz.muni.fi.airportmanager.baggageservice.resources;

import cz.muni.fi.airportmanager.baggageservice.entity.Baggage;
import cz.muni.fi.airportmanager.baggageservice.model.BaggageStatus;
import cz.muni.fi.airportmanager.baggageservice.model.CreateBaggageDto;
import cz.muni.fi.airportmanager.baggageservice.service.BaggageService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestHTTPEndpoint(BaggageResource.class)
class BaggageResourceTest {

    @InjectMock
    BaggageService baggageService;

    @Test
    void shouldGetEmptyListOfBaggage() {
        Mockito.when(this.baggageService.listAll()).thenReturn(Uni.createFrom().item(List.of()));

        given().when()
                .get()
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void shouldGetListOfBaggage() {
        var baggage = getBaggage();

        Mockito.when(this.baggageService.listAll()).thenReturn(Uni.createFrom().item(List.of(baggage)));

        given().when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void shouldCreateBaggage() {
        var baggage = getBaggage();

        CreateBaggageDto createBaggageDto = new CreateBaggageDto();
        createBaggageDto.weight = baggage.weight;
        createBaggageDto.passengerId = baggage.passengerId;

        Mockito.when(this.baggageService.createBaggage(Mockito.any(CreateBaggageDto.class))).thenReturn(Uni.createFrom().item(baggage));

        given().contentType("application/json")
                .body(createBaggageDto)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", equalTo(1));
    }

    @Test
    void shouldGetExistingBaggage() {
        var baggage = getBaggage();

        Mockito.when(this.baggageService.getBaggage(baggage.id)).thenReturn(Uni.createFrom().item(baggage));

        given().when()
                .get("/" + baggage.id)
                .then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @Test
    void shouldNotGetNonExistingBaggage() {
        Mockito.when(this.baggageService.getBaggage(Mockito.anyLong())).thenReturn(Uni.createFrom().failure(new IllegalArgumentException()));

        given().when()
                .get("/99") // Assuming ID 99 does not exist
                .then()
                .statusCode(404);
    }

    @Test
    void shouldDeleteAllBaggage() {
        Mockito.when(this.baggageService.deleteAllBaggage()).thenReturn(Uni.createFrom().item(0L));

        given().when()
                .delete()
                .then()
                .statusCode(200);
    }

    @Test
    void shouldClaimBaggage() {
        Mockito.when(this.baggageService.claimBaggage(Mockito.anyLong())).thenReturn(Uni.createFrom().item(true));

        given().when()
                .put("/1/claim") // Assuming ID 1 exists
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotClaimNonExistingBaggage() {
        Mockito.when(this.baggageService.claimBaggage(Mockito.anyLong())).thenReturn(Uni.createFrom().item(false));

        given().when()
                .put("/99/claim") // Assuming ID 99 does not exist
                .then()
                .statusCode(404);
    }

    @Test
    void shouldMarkBaggageAsLost() {
        Mockito.when(this.baggageService.lostBaggage(Mockito.anyLong())).thenReturn(Uni.createFrom().item(true));

        given().when()
                .put("/1/lost") // Assuming ID 1 exists
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotMarkNonExistingBaggageAsLost() {
        Mockito.when(this.baggageService.lostBaggage(Mockito.anyLong())).thenReturn(Uni.createFrom().item(false));

        given().when()
                .put("/99/lost") // Assuming ID 99 does not exist
                .then()
                .statusCode(404);
    }

    @Test
    void shouldGetBaggageByPassengerId() {
        var baggage = getBaggage();

        Mockito.when(this.baggageService.getBaggageByPassengerId(baggage.passengerId)).thenReturn(Uni.createFrom().item(List.of(baggage)));

        given().when()
                .get("/passenger/" + baggage.passengerId)
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    private static Baggage getBaggage() {
        var baggage = new Baggage();
        baggage.id = 1L;
        baggage.weight = 20;
        baggage.passengerId = 123L;
        baggage.status = BaggageStatus.CHECKED_IN;
        return baggage;
    }
}