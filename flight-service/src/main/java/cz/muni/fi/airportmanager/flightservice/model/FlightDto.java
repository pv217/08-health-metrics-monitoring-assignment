package cz.muni.fi.airportmanager.flightservice.model;


public class FlightDto extends CreateFlightDto {
    public long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FlightDto flightDto = (FlightDto) o;

        return id == flightDto.id;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}