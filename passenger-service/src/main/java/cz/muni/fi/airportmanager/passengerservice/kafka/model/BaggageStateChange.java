package cz.muni.fi.airportmanager.passengerservice.kafka.model;



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
