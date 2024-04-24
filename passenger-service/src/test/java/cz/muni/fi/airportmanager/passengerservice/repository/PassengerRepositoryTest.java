package cz.muni.fi.airportmanager.passengerservice.repository;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.entity.Passenger;
import cz.muni.fi.airportmanager.passengerservice.model.NotificationDto;
import io.quarkus.test.TestReactiveTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.UniAsserter;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class PassengerRepositoryTest {

    @Inject
    PassengerRepository passengerRepository;

    @Test
    @TestReactiveTransaction
    void shouldFindNotificationsForPassenger(UniAsserter asserter) {

        Passenger passenger = createTestPassenger();
        Notification notification = createTestNotification();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .execute(() -> {
                    passenger.addNotification(notification);
                    return passengerRepository.persist(passenger);
                })
                .assertThat(
                        () -> passengerRepository.findNotificationsForPassenger(passenger.getId()),
                        notifications -> {
                            assertEquals(1, notifications.size());
                            assertEquals(notification.message, notifications.get(0).message);
                        }
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldAddNotificationByFlightId(UniAsserter asserter) {

        // It should test that the notification is added to the appropriate passengers
        Passenger passenger = createTestPassenger();
        Notification notification = createTestNotification();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .execute(() -> passengerRepository.addNotificationByFlightId(passenger.getFlightId(), notification))
                .assertThat(
                        () -> passengerRepository.findNotificationsForPassenger(passenger.getId()),
                        notifications -> assertTrue(notifications.stream().anyMatch(n -> n.message.equals(notification.message)))
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldAddNotificationForPassenger(UniAsserter asserter) {

        // It should test that the notification is added to the appropriate passenger
        Passenger passenger = createTestPassenger();
        Notification notification = createTestNotification();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .execute(() -> passengerRepository.addNotificationForPassenger(passenger.getId(), notification))
                .assertThat(
                        () -> passengerRepository.findNotificationsForPassenger(passenger.getId()),
                        notifications -> assertTrue(notifications.stream().anyMatch(n -> n.message.equals(notification.message)))
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldFindPassengersForFlight(UniAsserter asserter) {

        // Persist a passenger and then get the its record by flight id

        Passenger passenger = createTestPassenger();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .assertThat(
                        () -> passengerRepository.findPassengersForFlight(passenger.getFlightId()),
                        passengers -> {
                            assertEquals(1, passengers.size());
                            assertEquals(passenger.getId(), passengers.get(0).getId());
                        }
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldFindNotificationsWithEmail(UniAsserter asserter) {

        // this test should find all notifications with the email of the passenger
        Passenger passenger = createTestPassenger();
        Notification notification = createTestNotification();
        passenger.addNotification(notification);

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .assertThat(
                        passengerRepository::findNotificationsWithEmail,
                        notificationDtos -> {
                            assertEquals(1, notificationDtos.size());
                            NotificationDto dto = notificationDtos.get(0);
                            assertEquals(notification.message, dto.message);
                            assertEquals(passenger.getEmail(), dto.email);
                        }
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldHandleNoNotificationsForPassenger(UniAsserter asserter) {

        Passenger passenger = createTestPassenger();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .assertThat(
                        () -> passengerRepository.findNotificationsForPassenger(passenger.getId()),
                        notifications -> assertEquals(0, notifications.size())
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldHandleInvalidPassengerIdForNotifications(UniAsserter asserter) {

        asserter.assertThat(
                () -> passengerRepository.findNotificationsForPassenger(-1L),
                notifications -> assertEquals(0, notifications.size())
        );
    }

    @Test
    @TestReactiveTransaction
    void shouldNotAddNotificationToNonExistentFlight(UniAsserter asserter) {

        Notification notification = createTestNotification();

        asserter
                .execute(() -> passengerRepository.addNotificationByFlightId(-1L, notification))
                .assertThat(
                        () -> passengerRepository.findNotificationsWithEmail(),
                        notifications -> assertEquals(0, notifications.size())
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldHandleNoPassengersForFlight(UniAsserter asserter) {

        asserter
                .assertThat(
                        () -> passengerRepository.findPassengersForFlight(-1L),
                        passengers -> assertEquals(0, passengers.size())
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldHandleEmptyPassengerRepositoryForNotificationsWithEmail(UniAsserter asserter) {

        asserter
                .assertThat(
                        passengerRepository::findNotificationsWithEmail,
                        notifications -> assertEquals(0, notifications.size())
                );
    }


    @Test
    @TestReactiveTransaction
    void shouldNotFindNonExistentPassenger(UniAsserter asserter) {

        asserter
                .assertThat(
                        () -> passengerRepository.findById(-1L),
                        Assertions::assertNull
                );
    }

    @Test
    @TestReactiveTransaction
    void shouldDeletePassenger(UniAsserter asserter) {

        Passenger passenger = createTestPassenger();

        asserter
                .execute(() -> passengerRepository.persist(passenger))
                .execute(() -> passengerRepository.deleteById(passenger.getId()))
                .assertThat(
                        () -> passengerRepository.findById(passenger.getId()),
                        Assertions::assertNull
                );
    }

    private Passenger createTestPassenger() {
        Passenger passenger = new Passenger();
        passenger.setFirstName("John");
        passenger.setLastName("Doe");
        passenger.setEmail("johndoe@example.com");
        passenger.setFlightId(1L);
        return passenger;
    }

    private Notification createTestNotification() {
        Notification notification = new Notification();
        notification.message = "Test notification message";
        return notification;
    }
}
