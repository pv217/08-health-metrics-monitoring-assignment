package cz.muni.fi.airportmanager.baggageservice.entity;


import cz.muni.fi.airportmanager.baggageservice.model.BaggageStatus;
import cz.muni.fi.airportmanager.baggageservice.model.CreateBaggageDto;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.Entity;

@Entity
public class Baggage extends PanacheEntity {
    public int weight;
    public Long passengerId;
    public BaggageStatus status;

    public static Uni<Baggage> findBaggageById(Long id) {
        return findById(id);
    }

    public static Baggage fromDto(CreateBaggageDto createBaggageDto) {
        var baggage = new Baggage();
        baggage.weight = createBaggageDto.weight;
        baggage.passengerId = createBaggageDto.passengerId;
        baggage.status = BaggageStatus.CHECKED_IN;
        return baggage;
    }
}