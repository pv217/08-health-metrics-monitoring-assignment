package cz.muni.fi.airportmanager.passengerservice.service;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.entity.Passenger;
import cz.muni.fi.airportmanager.passengerservice.model.CreatePassengerDto;
import cz.muni.fi.airportmanager.passengerservice.repository.PassengerRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class PassengerServiceTest {

    @InjectMock
    PassengerRepository passengerRepository;

    @Inject
    PassengerService passengerService;


    private Notification createNotification() {
        Notification notification = new Notification();
        notification.message = "Test message";
        return notification;
    }

    @Test
    @RunOnVertxContext
    void shouldGetListOfPassengers(UniAsserter asserter) {

        var passenger = createTestPassenger();
        asserter.execute(() -> Mockito.when(passengerRepository.listAll()).thenReturn(Uni.createFrom().item(List.of(passenger))));

        asserter.assertThat(
                () -> passengerService.listAll(),
                passengers -> {
                    assertNotNull(passengers);
                    assertFalse(passengers.isEmpty());
                    assertEquals(1, passengers.size());
                    assertEquals(passenger, passengers.get(0));
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldGetExistingPassenger(UniAsserter asserter) {

        var passenger = createTestPassenger();
        asserter.execute(() -> Mockito.when(passengerRepository.findById(passenger.getId())).thenReturn(Uni.createFrom().item(passenger)));

        asserter.assertThat(
                () -> passengerService.getPassenger(passenger.getId()),
                found -> {
                    assertNotNull(found);
                    assertEquals(passenger, found);
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldGetPassengersForFlight(UniAsserter asserter) {

        var passenger = createTestPassenger();
        Long flightId = 123L;
        asserter.execute(() -> Mockito.when(passengerRepository.findPassengersForFlight(flightId)).thenReturn(Uni.createFrom().item(List.of(passenger))));

        asserter.assertThat(
                () -> passengerService.getPassengersForFlight(flightId),
                passengers -> {
                    assertNotNull(passengers);
                    assertFalse(passengers.isEmpty());
                    assertEquals(1, passengers.size());
                    assertEquals(passenger, passengers.get(0));
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldCreatePassenger(UniAsserter asserter) {

        var passengerDto = createTestPassengerDto();
        var passenger = Passenger.fromDto(passengerDto);
        asserter.execute(() -> Mockito.when(passengerRepository.persist(passenger)).thenReturn(Uni.createFrom().item(passenger)));

        asserter.assertThat(
                () -> passengerService.createPassenger(passengerDto),
                created -> {
                    assertNotNull(created);
                    assertEquals(passenger, created);
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldDeleteExistingPassenger(UniAsserter asserter) {

        Long passengerId = 1L;
        asserter.execute(() -> Mockito.when(passengerRepository.deleteById(passengerId)).thenReturn(Uni.createFrom().item(true)));

        asserter.assertTrue(
                () -> passengerService.deletePassenger(passengerId)
        );
    }

    @Test
    @RunOnVertxContext
    void shouldDeleteAllPassengers(UniAsserter asserter) {

        asserter.execute(() -> Mockito.when(passengerRepository.deleteAll()).thenReturn(Uni.createFrom().item(1L)));

        asserter.assertThat(
                () -> passengerService.deleteAllPassengers(),
                count -> {
                    assertNotNull(count);
                    assertEquals(1L, count);
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldAddNotificationByFlightId(UniAsserter asserter) {

        Long flightId = 123L;
        Long passengerId = 1L;
        var notification = createNotification();

        // Mock the behavior of the repository
        asserter.execute(() -> Mockito.when(passengerRepository.addNotificationByFlightId(flightId, notification)).thenReturn(Uni.createFrom().voidItem()));
        asserter.execute(() -> Mockito.when(passengerRepository.findNotificationsForPassenger(passengerId)).thenReturn(Uni.createFrom().item(List.of(notification))));

        asserter.execute(() -> passengerService.addNotificationByFlightId(flightId, notification))
                .assertThat(
                () -> passengerService.findNotificationsForPassenger(passengerId),
                notifications -> assertTrue(notifications.stream().anyMatch(n -> n.message.equals(notification.message)))
        );
    }

    @Test
    @RunOnVertxContext
    void shouldAddNotificationForPassenger(UniAsserter asserter) {

        Long passengerId = 1L;
        var notification = createNotification();
        // Mock the behavior of the repository
        asserter.execute(() -> Mockito.when(passengerRepository.addNotificationForPassenger(passengerId, notification)).thenReturn(Uni.createFrom().voidItem()));
        asserter.execute(() -> Mockito.when(passengerRepository.findNotificationsForPassenger(passengerId)).thenReturn(Uni.createFrom().item(List.of(notification))));

        asserter.execute(() -> passengerService.addNotificationForPassenger(passengerId, notification))
                .assertThat(
                () -> passengerService.findNotificationsForPassenger(passengerId),
                notifications -> assertTrue(notifications.stream().anyMatch(n -> n.message.equals(notification.message)))
        );
    }

    @Test
    @RunOnVertxContext
    void shouldFindNotificationsForPassenger(UniAsserter asserter) {

        Long passengerId = 1L;
        var notification = createNotification();
        List<Notification> testNotifications = List.of(notification);
        // Mock the behavior of the repository
        asserter.execute(() -> Mockito.when(passengerRepository.findNotificationsForPassenger(passengerId)).thenReturn(Uni.createFrom().item(testNotifications)));

        asserter.assertThat(
                () -> passengerService.findNotificationsForPassenger(passengerId),
                notifications -> {
                    assertNotNull(notifications);
                    assertFalse(notifications.isEmpty());
                    assertEquals(testNotifications.size(), notifications.size());
                    assertEquals(testNotifications, notifications);
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldHandleNoPassengerFound(UniAsserter asserter) {

        Long invalidId = -1L;
        asserter.execute(() -> Mockito.when(passengerRepository.findById(invalidId)).thenReturn(Uni.createFrom().nullItem()));

        asserter.assertThat(
                () -> passengerService.getPassenger(invalidId),
                Assertions::assertNull
        );
    }

    @Test
    @RunOnVertxContext
    void shouldNotDeleteNonExistentPassenger(UniAsserter asserter) {

        Long invalidId = -1L;
        asserter.execute(() -> Mockito.when(passengerRepository.deleteById(invalidId)).thenReturn(Uni.createFrom().item(false)));

        asserter.assertFalse(
                () -> passengerService.deletePassenger(invalidId)
        );
    }

    @Test
    @RunOnVertxContext
    void shouldHandleEmptyListOfPassengers(UniAsserter asserter) {

        asserter.execute(() -> Mockito.when(passengerRepository.listAll()).thenReturn(Uni.createFrom().item(List.of())));

        asserter.assertThat(
                passengerService::listAll,
                passengers -> assertTrue(passengers.isEmpty())
        );
    }

    private Passenger createTestPassenger() {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setFlightId(1L);
        passenger.setEmail("john@gmail.com");
        return passenger;
    }

    private CreatePassengerDto createTestPassengerDto() {
        CreatePassengerDto passengerDto = new CreatePassengerDto();
        passengerDto.firstName = "John";
        passengerDto.lastName = "Doe";
        passengerDto.flightId = 1L;
        passengerDto.email = "john@gmail.com";
        return passengerDto;
    }
}
