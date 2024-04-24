package cz.muni.fi.airportmanager.passengerservice;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Passenger Service API",
                version = "0.1.0",
                description = "API for managing passengers and their notifications"
        )
)
public class PassengerServiceApplication extends Application {

}
