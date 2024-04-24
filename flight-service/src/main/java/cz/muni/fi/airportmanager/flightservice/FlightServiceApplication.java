package cz.muni.fi.airportmanager.flightservice;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Flight service API",
                version = "0.1.0",
                description = "This service provides operations to manage flights."
        )
)
public class FlightServiceApplication extends Application {

}
