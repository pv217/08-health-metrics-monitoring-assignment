package cz.muni.fi.airportmanager.baggageservice.service;

import cz.muni.fi.airportmanager.baggageservice.entity.Baggage;
import cz.muni.fi.airportmanager.baggageservice.kafka.model.BaggageStateChange;
import cz.muni.fi.airportmanager.baggageservice.kafka.producer.BaggageStateChangeProducer;
import cz.muni.fi.airportmanager.baggageservice.model.BaggageStatus;
import cz.muni.fi.airportmanager.baggageservice.model.CreateBaggageDto;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BaggageService {

    @Inject
    BaggageStateChangeProducer baggageStateChangeProducer;


    @WithTransaction
    public Uni<List<Baggage>> listAll() {
        return Baggage.listAll();
    }

    /**
     * Get baggage by id
     *
     * @param id baggage id
     * @return baggage with given id
     * @throws IllegalArgumentException if baggage with given id does not exist
     */
    @WithTransaction
    public Uni<Baggage> getBaggage(Long id) {
        return Baggage.findBaggageById(id).onItem().transform(Unchecked.function(baggage -> {
            if (baggage == null) {
                throw new IllegalArgumentException("Baggage with id " + id + " does not exist");
            }
            return baggage;
        }));
    }


    @WithTransaction
    public Uni<Baggage> createBaggage(CreateBaggageDto createBaggageDto) {
        var baggage = Baggage.fromDto(createBaggageDto);

        return baggage.persist().onItem().transform(
                persistedBaggage -> {
                    baggageStateChangeProducer.send((Baggage) persistedBaggage);
                    return (Baggage) persistedBaggage;
                }
        );
    }


    /**
     * Delete all baggage
     *
     * @return number of deleted baggage
     */
    @WithTransaction
    public Uni<Long> deleteAllBaggage() {
        return Baggage.deleteAll();
    }

    /**
     * Claim baggage
     *
     * @param id baggage id
     * @return if the baggage was cancelled
     */
    @WithTransaction
    public Uni<Boolean> claimBaggage(Long id) {
        return Baggage.findBaggageById(id).onItem().transformToUni(baggage -> {
            if (baggage == null) {
                return Uni.createFrom().failure(new IllegalArgumentException("Baggage with id " + id + " does not exist"));
            }
            baggage.status = BaggageStatus.CLAIMED;
            baggageStateChangeProducer.send(baggage);
            return Baggage.persist(baggage).replaceWith(true);
        });
    }

    /**
     * Get baggage by passenger id
     *
     * @param passengerId passenger id
     * @return list of baggage for passenger with given id
     */
    @WithTransaction
    public Uni<List<Baggage>> getBaggageByPassengerId(Long passengerId) {
        return Baggage.find("passengerId", passengerId).list();
    }

    /**
     * Mark baggage as lost
     *
     * @param id baggage id
     * @return if the baggage was marked as lost
     */
    @WithTransaction
    public Uni<Boolean> lostBaggage(Long id) {
        return Baggage.findBaggageById(id).onItem().transformToUni(baggage -> {
            if (baggage == null) {
                return Uni.createFrom().failure(new IllegalArgumentException("Baggage with id " + id + " does not exist"));
            }
            baggage.status = BaggageStatus.LOST;
            baggageStateChangeProducer.send(baggage);
            return Baggage.persist(baggage).replaceWith(true);
        });
    }
}
