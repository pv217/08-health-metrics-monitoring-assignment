package cz.muni.fi.airportmanager.flightservice.repository;

import cz.muni.fi.airportmanager.flightservice.entity.Flight;
import cz.muni.fi.airportmanager.flightservice.model.FlightStatus;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.List;

@ApplicationScoped
public class FlightRepository implements PanacheRepository<Flight> {

    /**
     * Find all future flights that are scheduled to depart in the future
     *
     * @param currentDate current date
     * @return list of all flights
     */
    @WithTransaction
    public Uni<List<Flight>> findFuture(Date currentDate) {
        return find("departureTime >= ?1", currentDate).list();
    }

    /**
     * Find all ongoing flights that are ongoing (departed but not arrived yet)
     *
     * @param currentDate current date
     * @return list of  flights
     */
    @WithTransaction
    public Uni<List<Flight>> findOngoingFlights(Date currentDate) {
        return find("departureTime <= ?1 and arrivalTime >= ?1", currentDate).list();
    }

    /**
     * Find all past flights that are scheduled to depart in the past
     *
     * @param currentDate current date
     * @return list of  flights
     */
    @WithTransaction
    public Uni<List<Flight>> findPastFlights(Date currentDate) {
        return find("arrivalTime < ?1", currentDate).list();
    }

    /**
     * Find flight by status
     *
     * @param status flight status
     * @return list of flights
     */
    @WithTransaction
    public Uni<List<Flight>> findByStatus(FlightStatus status) {
        return find("status", status).list();
    }

    /**
     * Change status of the flight
     *
     * @param flightId  flight id
     * @param newStatus new status
     * @throws IllegalArgumentException if flight with given id does not exist
     */
    @WithTransaction
    public Uni<Void> changeStatus(Long flightId, FlightStatus newStatus) {
        return findById(flightId)
                .onItem().transformToUni(flight -> {
                    if (flight == null) {
                        return Uni.createFrom().failure(new IllegalArgumentException("Flight with id " + flightId + " does not exist"));
                    }
                    flight.setStatus(newStatus);
                    return persist(flight).replaceWithVoid();
                });
    }
}
