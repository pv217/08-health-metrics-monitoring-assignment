package cz.muni.fi.airportmanager.passengerservice.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class Notification extends PanacheEntity {
    public String message;

    public Long passengerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (!Objects.equals(message, that.message)) return false;
        return Objects.equals(passengerId, that.passengerId);
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (passengerId != null ? passengerId.hashCode() : 0);
        return result;
    }
}
