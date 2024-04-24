package cz.muni.fi.airportmanager.passengerservice.resource;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.entity.Passenger;
import cz.muni.fi.airportmanager.passengerservice.model.CreatePassengerDto;
import cz.muni.fi.airportmanager.passengerservice.model.PassengerWithBaggageDto;
import cz.muni.fi.airportmanager.passengerservice.model.examples.Examples;
import cz.muni.fi.airportmanager.passengerservice.service.PassengerService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.List;
import java.util.Objects;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * This class is a REST resource that will be hosted on /passenger
 */
@Path("/passenger")
@Tag(name = "Passenger", description = "Passenger CRUD API")
public class PassengerResource {

    @Inject
    PassengerService passengerService;

    /**
     * Get list of all passengers
     *
     * @return list of all passengers
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get list of all passengers")
    @APIResponse(
            responseCode = "200",
            description = "List of all passengers",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Passenger.class, required = true),
                    examples = @ExampleObject(name = "flight", value = Examples.VALID_PASSENGER_LIST)
            )
    )
    public Uni<RestResponse<List<Passenger>>> list() {
        return passengerService.listAll().onItem().transform(passengers -> RestResponse.status(Response.Status.OK, passengers));
    }

    /**
     * Create a new passenger
     *
     * @param passenger passenger to create.
     * @return created passenger
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new passenger")
    @APIResponse(
            responseCode = "201",
            description = "Created passenger",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Passenger.class, required = true),
                    examples = @ExampleObject(name = "flight", value = Examples.VALID_PASSENGER)
            )
    )
    @Counted(value = "passenger_create_count", description = "How many times passenger was created")
    public Uni<RestResponse<Passenger>> create(
            @Schema(implementation = CreatePassengerDto.class, required = true)
            CreatePassengerDto passenger) {
        return passengerService.createPassenger(passenger)
                .onItem().transform(createdPassenger -> RestResponse.status(Response.Status.CREATED, createdPassenger));
    }

    /**
     * Get passenger by id
     *
     * @param id id of passenger
     * @return passenger with given id
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get passenger by id")
    @APIResponse(
            responseCode = "200",
            description = "Passenger with given id",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Passenger.class, required = true),
                    examples = @ExampleObject(name = "flight", value = Examples.VALID_PASSENGER)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Passenger with given id does not exist"
    )
    public Uni<RestResponse<Passenger>> get(@Parameter(name = "id", required = true, description = "Passenger id") @PathParam("id") Long id) {
        return passengerService.getPassenger(id)
                .onItem().transform(passenger -> {
                    if (passenger == null) {
                        return RestResponse.status(Response.Status.NOT_FOUND);
                    }
                    return RestResponse.status(Response.Status.OK, passenger);
                });
    }

    /**
     * Get passengers for a flight by flight id
     * @param flightId flight id
     *                 @return list of passengers for a flight
     */
    @GET
    @Path("/flight/{flightId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get passengers for a flight by flight id")
    @APIResponse(
            responseCode = "200",
            description = "List of passengers for a flight",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Passenger.class, required = true),
                    examples = @ExampleObject(name = "flight", value = Examples.VALID_PASSENGER_LIST)
            )
    )
    public Uni<RestResponse<List<Passenger>>> getPassengersForFlight(@Parameter(name = "flightId", required = true, description = "Flight id") @PathParam("flightId") Long flightId) {
        return passengerService.getPassengersForFlight(flightId)
                .onItem().transform(passengers -> RestResponse.status(Response.Status.OK, passengers));
    }

    /**
     * Get all notifications for a passenger
     * @param passengerId passenger id
     *                    @return list of notifications for a passenger
     */
    @GET
    @Path("/{passengerId}/notifications")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all notifications for a passenger")
    @APIResponse(
            responseCode = "200",
            description = "List of notifications for a passenger",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Passenger.class, required = true),
                    examples = @ExampleObject(name = "flight", value = Examples.VALID_NOTIFICATION_LIST)
            )
    )
    @Counted(value = "notification_get_count", description = "How many times notifications were retrieved")
    public Uni<RestResponse<List<Notification>>> getNotificationsForPassenger(@Parameter(name = "passengerId", required = true, description = "Passenger id") @PathParam("passengerId") Long passengerId) {
        return passengerService.findNotificationsForPassenger(passengerId)
                .onItem().transform(passengers -> RestResponse.status(Response.Status.OK, passengers));
    }

    /**
     * Delete passenger
     *
     * @param id id of passenger
     */
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete passenger")
    public Uni<RestResponse<Passenger>> delete(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        return passengerService.deletePassenger(id)
                .onItem().transform(deleted -> {
                    if (deleted) {
                        return RestResponse.status(Response.Status.OK);
                    }
                    return RestResponse.status(Response.Status.NOT_FOUND);
                });
    }

    /**
     * Helper method to delete all passengers
     */
    @DELETE
    @Operation(summary = "Delete all passengers")
    public Uni<RestResponse<Passenger>> deleteAll() {
        return passengerService.deleteAllPassengers()
                .onItem().transform(ignored -> RestResponse.status(Response.Status.OK));
    }

    /**
     * Get passenger with baggage
     */
    @GET
    @Path("/{passengerId}/baggage")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get passenger with baggage")
    @APIResponse(
            responseCode = "200",
            description = "Passenger with baggage",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = PassengerWithBaggageDto.class, required = true),
                    examples = @ExampleObject(name = "passenger with baggage", value = Examples.VALID_PASSENGER_WITH_BAGGAGE)
            )
    )
    @Timed(value = "get_passenger_with_baggage", description = "A measure of how long it takes to get passenger with baggage")
    public Uni<RestResponse<?>> getPassengerWithBaggage(@Parameter(name = "passengerId", required = true, description = "Passenger id") @PathParam("passengerId") Long passengerId) {
        return passengerService.getPassengerWithBaggage(passengerId)
                .onItem().transform(passenger -> {
                    if (Objects.isNull(passenger)) {
                        return RestResponse.status(Response.Status.NOT_FOUND);
                    }
                    return RestResponse.status(Response.Status.OK, passenger);
                });
    }


}
