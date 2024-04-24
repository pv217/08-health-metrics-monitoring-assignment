package cz.muni.fi.airportmanager.passengerservice.resource;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.model.NotificationDto;
import cz.muni.fi.airportmanager.passengerservice.service.NotificationService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@TestHTTPEndpoint(NotificationResource.class)
class NotificationResourceTest {

    @InjectMock
    NotificationService notificationService;


    @Test
    void shouldGetEmptyList() {
        Mockito.when(notificationService.listAll()).thenReturn(Uni.createFrom().item(List.of()));

        given().when()
                .get()
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    void shouldGetNotifications() {
        var notification = createNotificationDto();
        Mockito.when(notificationService.listAll()).thenReturn(Uni.createFrom().item(List.of(notification)));

        given().when()
                .get()
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("size()", is(1));
    }


    @Test
    void shouldDeleteAllNotifications() {
        Mockito.when(notificationService.deleteAll()).thenReturn(Uni.createFrom().item(1L));

        given().when()
                .delete()
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    private NotificationDto createNotificationDto() {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.id = 1L;
        notificationDto.message = "Test message";
        notificationDto.email = "test@test.com";
        return notificationDto;
    }

}