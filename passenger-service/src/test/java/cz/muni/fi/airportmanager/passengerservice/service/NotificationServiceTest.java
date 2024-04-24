package cz.muni.fi.airportmanager.passengerservice.service;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.model.NotificationDto;
import cz.muni.fi.airportmanager.passengerservice.repository.PassengerRepository;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class NotificationServiceTest {

    @Inject
    NotificationService notificationService;

    @InjectMock
    PassengerRepository passengerRepository;

    private Notification createNotification() {
        Notification notification = new Notification();
        notification.id = 1L;
        notification.message = "Test message";
        return notification;
    }

    private NotificationDto createNotificationDto() {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.id = 1L;
        notificationDto.message = "Test message";
        notificationDto.email = "test@test.com";
        return notificationDto;
    }

    @Test
    @TestReactiveTransaction
    void shouldGetListOfNotifications(UniAsserter asserter) {
        Notification notification = createNotification();
        var notificationDto = createNotificationDto();
//        Mock active record class
        asserter.execute(() -> PanacheMock.mock(Notification.class));
        asserter.execute(() -> Mockito.when(Notification.listAll()).thenReturn(Uni.createFrom().item(List.of(notification))));
        asserter.execute(() -> Mockito.when(passengerRepository.findNotificationsWithEmail()).thenReturn(Uni.createFrom().item(List.of(notificationDto))));

        asserter.assertThat(() -> notificationService.listAll(),
                notificationList -> {
                    assertNotNull(notificationList);
                    assertFalse(notificationList.isEmpty());
                    assertEquals(1, notificationList.size());
                    assertEquals(notificationDto, notificationList.get(0));
                });
    }


    @Test
    @TestReactiveTransaction
    void shouldDeleteAllNotifications(UniAsserter asserter) {
        asserter.execute(() -> PanacheMock.mock(Notification.class));
        asserter.execute(() -> Mockito.when(Notification.deleteAll()).thenReturn(Uni.createFrom().item(1L)));

        asserter.assertThat(() -> notificationService.deleteAll(),
                deletedCount -> {
                    assertNotNull(deletedCount);
                    assertEquals(1L, deletedCount);
                });
    }
}
