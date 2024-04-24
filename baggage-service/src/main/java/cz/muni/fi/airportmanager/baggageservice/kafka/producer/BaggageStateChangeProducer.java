package cz.muni.fi.airportmanager.baggageservice.kafka.producer;

import cz.muni.fi.airportmanager.baggageservice.entity.Baggage;
import cz.muni.fi.airportmanager.baggageservice.kafka.model.BaggageStateChange;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class BaggageStateChangeProducer {

    @Channel("baggage-state-change")
    Emitter<BaggageStateChange> emitter;

    /**
     * Send baggage state change to Kafka
     *
     * @param baggage baggage to send
     */
    public void send(Baggage baggage) {
        BaggageStateChange baggageStateChange = new BaggageStateChange();
        baggageStateChange.baggageId = baggage.id;
        baggageStateChange.passengerId = baggage.passengerId;
        baggageStateChange.newStatus = baggage.status;
        emitter.send(baggageStateChange);
    }
}
