package cz.muni.fi.airportmanager.flightservice.model;

public enum FlightStatus {
    ACTIVE("ACTIVE"),
    BOARDING("BOARDING"),
    CANCELLED("CANCELLED"),
    DELAYED("DELAYED"),
    COMPLETED("COMPLETED");

    private final String status;

    FlightStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}


