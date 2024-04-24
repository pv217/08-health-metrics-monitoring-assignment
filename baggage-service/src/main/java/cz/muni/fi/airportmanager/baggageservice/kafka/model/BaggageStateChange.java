package cz.muni.fi.airportmanager.baggageservice.kafka.model;

import cz.muni.fi.airportmanager.baggageservice.model.BaggageStatus;

public class BaggageStateChange {

    public Long baggageId;
    public Long passengerId;
    public BaggageStatus newStatus;

    public BaggageStateChange() {
    }

    public BaggageStateChange(Long baggageId, Long passengerId, BaggageStatus newStatus) {
        this.baggageId = baggageId;
        this.passengerId = passengerId;
        this.newStatus = newStatus;
    }

    @Override
    public String toString() {
        return "BaggageStateChange{" +
                "baggageId=" + baggageId +
                ", passengerId=" + passengerId +
                ", newStatus=" + newStatus +
                '}';
    }
}
