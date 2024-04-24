package cz.muni.fi.airportmanager.flightservice.service;

import cz.muni.fi.airportmanager.flightservice.entity.Flight;
import cz.muni.fi.airportmanager.flightservice.model.CreateFlightDto;
import cz.muni.fi.airportmanager.flightservice.model.FlightDto;
import cz.muni.fi.airportmanager.flightservice.model.FlightStatus;
import cz.muni.fi.airportmanager.flightservice.repository.FlightRepository;
import cz.muni.fi.airportmanager.proto.FlightCancellationRequest;
import cz.muni.fi.airportmanager.proto.FlightCancellationResponseStatus;
import cz.muni.fi.airportmanager.proto.MutinyFlightCancellationGrpc;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FlightService {
    @Inject
    FlightRepository flightRepository;


    @GrpcClient("passenger-service")
    MutinyFlightCancellationGrpc.MutinyFlightCancellationStub flightCancellationStub;

    /**
     * Get list of all flights
     *
     * @return list of all flights
     */
    @WithTransaction
    public Uni<List<FlightDto>> listAll() {
        return flightRepository.listAll().onItem().transform(flights -> flights.stream().map(Flight::toDto).toList());
    }

    /**
     * Get flight by id
     *
     * @param id flight id
     * @return flight with given id
     * @throws IllegalArgumentException if flight with given id does not exist
     */
    @WithTransaction
    public Uni<FlightDto> getFlight(Long id) {
        return flightRepository.findById(id).onItem().transform(Unchecked.function(flight -> {
            if (flight == null) {
                throw new IllegalArgumentException("Flight with id " + id + " does not exist");
            }
            return flight.toDto();
        }));
    }

    /**
     * Create a new flight
     *
     * @param flight flight to create.
     * @return created flight
     */
    @WithTransaction
    public Uni<FlightDto> createFlight(CreateFlightDto flight) {
        return flightRepository.persist(Flight.fromDto(flight)).onItem().transform(Flight::toDto);
    }


    /**
     * Delete flight
     *
     * @param id flight id
     * @return if the flight was deleted
     */
    @WithTransaction
    public Uni<Boolean> deleteFlight(Long id) {
        return flightRepository.deleteById(id);
    }

    /**
     * Delete all flights
     *
     * @return number of deleted flights
     */
    @WithTransaction
    public Uni<Long> deleteAllFlights() {
        return flightRepository.deleteAll();
    }

    /**
     * Cancel flight
     *
     * @param id flight id
     * @return if the flight was cancelled
     */
    @WithTransaction
    public Uni<Boolean> cancelFlight(Long id) {
        return flightRepository.changeStatus(id, FlightStatus.CANCELLED)
                .onItem().transformToUni(ignored ->
                        flightCancellationStub.cancelFlight(
                                FlightCancellationRequest.newBuilder()
                                        .setId(Math.toIntExact(id))
                                        .setReason("Unknown")
                                        .build()
                        )
                )
                .onItem().transform(response -> {
                    if (response.getStatus() != FlightCancellationResponseStatus.Cancelled) {
                        throw new RuntimeException("Flight cancellation failed");
                    }
                    return true;
                });
//                .onFailure().recoverWithItem(false);
    }
}
