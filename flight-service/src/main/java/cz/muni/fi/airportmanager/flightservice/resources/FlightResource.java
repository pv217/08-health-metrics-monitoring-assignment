package cz.muni.fi.airportmanager.flightservice.resources;

import cz.muni.fi.airportmanager.flightservice.model.CreateFlightDto;
import cz.muni.fi.airportmanager.flightservice.model.example.Examples;
import cz.muni.fi.airportmanager.flightservice.service.FlightService;
import cz.muni.fi.airportmanager.flightservice.model.FlightDto;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * This class is a REST resource that will be hosted on /flight
 */
@Path("/flight")
@Tag(name = "Flight", description = "Flight CRUD API")
public class FlightResource {

    @Inject
    FlightService flightService;


    /**
     * Get list of all flights
     *
     * @return list of all flights
     */
    @GET
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Get list of all flights")
    @APIResponse(
            responseCode = "200",
            description = "Get list of all flights",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = FlightDto.class, required = true),
                    examples = @ExampleObject(name = "flight", value = Examples.VALID_FLIGHT_LIST)
            )
    )
    public Uni<RestResponse<List<FlightDto>>> list() {
        return flightService.listAll()
                .onItem().transform(flights -> RestResponse.status(Response.Status.OK, flights));
    }

    /**
     * Create a new flight
     *
     * @param flight flight to create.
     * @return created flight
     */
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Operation(summary = "Create a new flight")
    @APIResponse(
            responseCode = "201",
            description = "Created flight",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = FlightDto.class, required = true),
                    examples = @ExampleObject(name = "flight", value = Examples.VALID_FLIGHT)
            )
    )
    @APIResponse(
            responseCode = "409",
            description = "Conflict"
    )
    // TODO Add @Counted annotation to count how many flights have been created. The name of the counter should be "flight_create"
    public Uni<RestResponse<FlightDto>> create(CreateFlightDto flight) {
        return flightService.createFlight(flight)
                .onItem().transform(newFlight -> RestResponse.status(Response.Status.CREATED, newFlight))
                .onFailure(IllegalArgumentException.class).recoverWithItem(RestResponse.status(Response.Status.CONFLICT));
    }


    /**
     * Get flight by id
     *
     * @param id id of flight
     * @return flight with given id
     */
    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Get flight by id")
    @APIResponse(
            responseCode = "200",
            description = "Flight with given id",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = FlightDto.class, required = true),
                    examples = @ExampleObject(name = "flight", value = Examples.VALID_FLIGHT)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Flight with given id does not exist"
    )
    public Uni<RestResponse<FlightDto>> get(long id) {
        return flightService.getFlight(id)
                .onItem().transform(flight -> RestResponse.status(Response.Status.OK, flight))
                .onFailure(IllegalArgumentException.class).recoverWithItem(RestResponse.status(Response.Status.NOT_FOUND));
    }


    /**
     * Delete flight
     *
     * @param id id of flight
     */
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete flight")
    @APIResponse(
            responseCode = "200",
            description = "Flight deleted"
    )
    @APIResponse(
            responseCode = "404",
            description = "Flight with given id does not exist"
    )
    public Uni<RestResponse<Object>> delete(@Parameter(name = "id", required = true) @PathParam("id") long id) {
        return flightService.deleteFlight(id)
                .onItem().transform(wasDeleted -> {
                    if (Boolean.TRUE.equals(wasDeleted)) {
                        return
                                RestResponse.status(Response.Status.OK);
                    }
                    return RestResponse.status(Response.Status.NOT_FOUND);
                })
                .onFailure(IllegalArgumentException.class).recoverWithItem(RestResponse.status(Response.Status.NOT_FOUND));
    }

    /**
     * Helper method for to delete all flights
     */
    @DELETE
    @Operation(summary = "Delete all flights")
    @APIResponse(
            responseCode = "200",
            description = "All flights deleted"
    )
    public Uni<RestResponse<Void>> deleteAll() {
        return flightService.deleteAllFlights()
                .onItem().transform(ignored -> RestResponse.status(Response.Status.OK));
    }

    /**
     * Cancel flight
     */
    @PUT
    @Path("/{id}/cancel")
    @Operation(summary = "Cancel flight")
    @APIResponse(
            responseCode = "200",
            description = "Flight cancelled"
    )
    @APIResponse(
            responseCode = "404",
            description = "Flight with given id does not exist"
    )
    // TODO Add @Timer annotation to measure how long it takes to cancel a flight. The name of the timer should be "flight_cancel"
    public Uni<RestResponse<Object>> cancel(@Parameter(name = "id", required = true) @PathParam("id") long id) {
        return flightService.cancelFlight(id)
                .onItem().transform(wasCancelled -> {
                    if (Boolean.TRUE.equals(wasCancelled)) {
                        return RestResponse.status(Response.Status.OK);
                    }
                    return RestResponse.status(Response.Status.NOT_FOUND);
                });
    }

}

