package cz.muni.fi.airportmanager.flightservice.model;

import java.util.Date;
import java.util.Objects;

public class CreateFlightDto {
    public String name;
    public String airportFrom;
    public String airportTo;
    public Date departureTime;
    public Date arrivalTime;
    public int capacity;
    public FlightStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateFlightDto that = (CreateFlightDto) o;

        if (capacity != that.capacity) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(airportFrom, that.airportFrom)) return false;
        if (!Objects.equals(airportTo, that.airportTo)) return false;
        if (!Objects.equals(departureTime, that.departureTime))
            return false;
        if (!Objects.equals(arrivalTime, that.arrivalTime)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (airportFrom != null ? airportFrom.hashCode() : 0);
        result = 31 * result + (airportTo != null ? airportTo.hashCode() : 0);
        result = 31 * result + (departureTime != null ? departureTime.hashCode() : 0);
        result = 31 * result + (arrivalTime != null ? arrivalTime.hashCode() : 0);
        result = 31 * result + capacity;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
