package cz.muni.fi.airportmanager.passengerservice.client;

import cz.muni.fi.airportmanager.passengerservice.model.Baggage;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

/**
 * Client for baggage service
 */
@RegisterRestClient(configKey = "baggage-resource")
public interface BaggageClientResource {

    @GET
    @Path("/baggage/passenger/{passengerId}")
    Uni<List<Baggage>> getBaggageForPassengerId(@PathParam("passengerId") Long passengerId);

    @GET
    @Path("/q/health/ready")
    Uni<HealthCheckResponse> readinessCheck();
}
