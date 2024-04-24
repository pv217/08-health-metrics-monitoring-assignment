package cz.muni.fi.airportmanager.baggageservice;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Baggage service API",
                version = "0.1.0",
                description = "This service tracks baggage and their status. It is used to manage baggage in the airport."
        )
)
public class BaggageServiceApplication extends Application {

}
