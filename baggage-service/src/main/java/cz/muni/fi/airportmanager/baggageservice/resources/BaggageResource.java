package cz.muni.fi.airportmanager.baggageservice.resources;

import cz.muni.fi.airportmanager.baggageservice.entity.Baggage;
import cz.muni.fi.airportmanager.baggageservice.model.CreateBaggageDto;
import cz.muni.fi.airportmanager.baggageservice.model.example.Examples;
import cz.muni.fi.airportmanager.baggageservice.service.BaggageService;
import io.micrometer.core.annotation.Counted;
import io.smallrye.mutiny.Uni;
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
 * This class is a REST resource that will be hosted on /baggage
 */
@Path("/baggage")
@Tag(name = "Baggage", description = "Baggage API")
public class BaggageResource {

    @Inject
    BaggageService baggageService;


    @GET
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Get list of all baggage")
    @APIResponse(
            responseCode = "200",
            description = "Get list of all baggage",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Baggage.class, required = true),
                    examples = @ExampleObject(name = "baggage", value = Examples.VALID_BAGGAGE_LIST)
            )
    )
    @Counted(value = "baggage_list_count", description = "How many times baggage list was requested")
    public Uni<RestResponse<List<Baggage>>> list() {
        return baggageService.listAll()
                .onItem().transform(baggage -> RestResponse.status(Response.Status.OK, baggage));
    }


    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    @Operation(summary = "Create a new baggage")
    @APIResponse(
            responseCode = "201",
            description = "Created baggage",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Baggage.class, required = true),
                    examples = @ExampleObject(name = "baggage", value = Examples.VALID_BAGGAGE)
            )
    )
    @APIResponse(
            responseCode = "409",
            description = "Conflict"
    )
    @Counted(value = "baggage_create", description = "How many baggage have been created")
    public Uni<RestResponse<Baggage>> create(CreateBaggageDto baggage) {
        return baggageService.createBaggage(baggage)
                .onItem().transform(newBaggage -> RestResponse.status(Response.Status.CREATED, newBaggage))
                .onFailure(IllegalArgumentException.class).recoverWithItem(RestResponse.status(Response.Status.CONFLICT));
    }


    @GET
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Get baggage by id")
    @APIResponse(
            responseCode = "200",
            description = "Baggage with given id",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Baggage.class, required = true),
                    examples = @ExampleObject(name = "baggage", value = Examples.VALID_BAGGAGE)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "Baggage with given id does not exist"
    )
    public Uni<RestResponse<Baggage>> get(long id) {
        return baggageService.getBaggage(id)
                .onItem().transform(baggage -> RestResponse.status(Response.Status.OK, baggage))
                .onFailure(IllegalArgumentException.class).recoverWithItem(RestResponse.status(Response.Status.NOT_FOUND));
    }


    @DELETE
    @Operation(summary = "Delete all baggage")
    @APIResponse(
            responseCode = "200",
            description = "All baggage deleted"
    )
    public Uni<RestResponse<Void>> deleteAll() {
        return baggageService.deleteAllBaggage()
                .onItem().transform(ignored -> RestResponse.status(Response.Status.OK));
    }

    /**
     * Claim baggage
     */
    @PUT
    @Path("/{id}/claim")
    @Operation(summary = "Claim baggage")
    @APIResponse(
            responseCode = "200",
            description = "Baggage claimed"
    )
    @APIResponse(
            responseCode = "404",
            description = "Baggage with given id does not exist"
    )
    @Counted(value = "baggage_claim_count", description = "How many baggage have been claimed")
    public Uni<RestResponse<Object>> claim(@Parameter(name = "id", required = true) @PathParam("id") long id) {
        return baggageService.claimBaggage(id)
                .onItem().transform(wasClaimed -> {
                    if (Boolean.TRUE.equals(wasClaimed)) {
                        return RestResponse.status(Response.Status.OK);
                    }
                    return RestResponse.status(Response.Status.NOT_FOUND);
                });
    }

    /**
     * Mark baggage as lost
     */
    @PUT
    @Path("/{id}/lost")
    @Operation(summary = "Mark baggage as lost")
    @APIResponse(
            responseCode = "200",
            description = "Baggage marked as lost"
    )
    @APIResponse(
            responseCode = "404",
            description = "Baggage with given id does not exist"
    )
    @Counted(value = "baggage_lost_count", description = "How many baggage have been marked as lost")
    public Uni<RestResponse<Object>> lost(@Parameter(name = "id", required = true) @PathParam("id") long id) {
        return baggageService.lostBaggage(id)
                .onItem().transform(wasLost -> {
                    if (Boolean.TRUE.equals(wasLost)) {
                        return RestResponse.status(Response.Status.OK);
                    }
                    return RestResponse.status(Response.Status.NOT_FOUND);
                });
    }

    /**
     * Get baggage by passenger id
     */
    @GET
    @Path("/passenger/{passengerId}")
    @Produces(APPLICATION_JSON)
    @Operation(summary = "Get baggage by passenger id")
    @APIResponse(
            responseCode = "200",
            description = "Baggage with given passenger id",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Baggage.class, required = true),
                    examples = @ExampleObject(name = "baggage", value = Examples.VALID_BAGGAGE_LIST)
            )
    )
    public Uni<RestResponse<List<Baggage>>> getBaggageByPassengerId(@Parameter(name = "passengerId", required = true) @PathParam("passengerId") long passengerId) {
        return baggageService.getBaggageByPassengerId(passengerId)
                .onItem().transform(baggage -> RestResponse.status(Response.Status.OK, baggage));
    }

}

