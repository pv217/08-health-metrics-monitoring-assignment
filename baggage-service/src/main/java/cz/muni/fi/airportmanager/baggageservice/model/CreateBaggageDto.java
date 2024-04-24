package cz.muni.fi.airportmanager.baggageservice.model;

import java.util.Objects;

public class CreateBaggageDto {
    public int weight;
    public Long passengerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateBaggageDto that = (CreateBaggageDto) o;

        if (Double.compare(that.weight, weight) != 0) return false;
        return Objects.equals(passengerId, that.passengerId);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(weight);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (passengerId != null ? passengerId.hashCode() : 0);
        return result;
    }
}
