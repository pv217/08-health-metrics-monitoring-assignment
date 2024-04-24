package cz.muni.fi.airportmanager.flightservice.service;

import cz.muni.fi.airportmanager.flightservice.entity.Flight;
import cz.muni.fi.airportmanager.flightservice.model.CreateFlightDto;
import cz.muni.fi.airportmanager.flightservice.model.FlightStatus;
import cz.muni.fi.airportmanager.flightservice.repository.FlightRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import io.quarkus.test.vertx.UniAsserter;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.Duration;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class FlightServiceTest {

    @InjectMock
    FlightRepository flightRepository;

    @Inject
    FlightService flightService;


    @Test
    @RunOnVertxContext
        // Make sure the test method is run on the Vert.x event loop. aka support async
        // Gives us UniAsserter
    void shouldGetListOfFlights(UniAsserter asserter) {
        var flight = createOngoingFlight();
        asserter.execute(() -> Mockito.when(flightRepository.listAll()).thenReturn(Uni.createFrom().item(List.of(flight))));

        asserter.assertThat(
                () -> flightService.listAll(),
                flights -> {
                    assertNotNull(flights);
                    assertFalse(flights.isEmpty());
                    assertEquals(1L, flights.size());
                    assertEquals(flight.toDto(), flights.get(0));
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldGetExistingFlight(UniAsserter asserter) {
        var flight = createOngoingFlight();
        asserter.execute(() -> Mockito.when(flightRepository.findById(flight.getId())).thenReturn(Uni.createFrom().item(flight)));

        asserter.assertThat(
                () -> flightService.getFlight(flight.getId()),
                found -> {
                    assertNotNull(found);
                    assertEquals(flight.toDto(), found);
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldNotGetNonexistingFlight(UniAsserter asserter) {
        asserter.execute(() -> Mockito.when(flightRepository.findById(Mockito.any())).thenReturn(Uni.createFrom().failure(new IllegalArgumentException())));

        asserter.assertFailedWith(
                () -> flightService.getFlight(999L),
                IllegalArgumentException.class
        );
    }


    @Test
    @RunOnVertxContext
    void shouldDeleteExistingFlight(UniAsserter asserter) {
        var flight = createOngoingFlight();
        asserter.execute(() -> Mockito.when(flightRepository.deleteById(flight.getId())).thenReturn(Uni.createFrom().item(true)));

        asserter.assertThat(
                () -> flightService.deleteFlight(flight.getId()),
                Assertions::assertTrue
        );
    }

    @Test
    @RunOnVertxContext
    void shouldNotDeleteNonexistingFlight(UniAsserter asserter) {
        asserter.execute(() -> Mockito.when(flightRepository.deleteById(Mockito.any())).thenReturn(Uni.createFrom().item(false)));

        asserter.assertFalse(
                () -> flightService.deleteFlight(999L)
        );
    }

    @Test
    @RunOnVertxContext
    void shouldDeleteAllFlights(UniAsserter asserter) {
        asserter.execute(() -> Mockito.when(flightRepository.deleteAll()).thenReturn(Uni.createFrom().item(1L)));

        asserter.assertThat(
                () -> flightService.deleteAllFlights(),
                count -> {
                    assertNotNull(count);
                    assertEquals(1L, count);
                }
        );
    }

    @Test
    @RunOnVertxContext
    void shouldCreateFlight(UniAsserter asserter) {
        var flight = createOngoingFlight();
        var createFlightDto = new CreateFlightDto();
        createFlightDto.name = (flight.getName());
        createFlightDto.airportFrom = (flight.getAirportFrom());
        createFlightDto.airportTo = (flight.getAirportTo());
        createFlightDto.departureTime = (flight.getDepartureTime());
        createFlightDto.arrivalTime = (flight.getArrivalTime());
        createFlightDto.capacity = (flight.getCapacity());

        asserter.execute(() -> Mockito.when(flightRepository.persist(Mockito.any(Flight.class))).thenReturn(Uni.createFrom().item(flight)));

        asserter.assertThat(
                () -> flightService.createFlight(createFlightDto),
                created -> {
                    assertNotNull(created);
                    assertEquals(flight.toDto(), created);
                }
        );
    }


    private Flight createOngoingFlight() {
        var future = Date.from(java.time.Instant.now().plus(Duration.ofMinutes(1000 * 60)));
        var past = Date.from(java.time.Instant.now().minus(Duration.ofMinutes(1000 * 60)));
        var flight = new Flight();
        flight.setName("Test Flight");
        flight.setAirportFrom("Airport A");
        flight.setAirportTo("Airport B");
        flight.setDepartureTime(past);
        flight.setArrivalTime(future);
        flight.setCapacity(100);
        flight.setStatus(FlightStatus.ACTIVE);
        flight.setId(1L);

        return flight;
    }
}