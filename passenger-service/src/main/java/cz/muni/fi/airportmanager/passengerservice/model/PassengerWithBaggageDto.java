package cz.muni.fi.airportmanager.passengerservice.model;

import java.util.List;

public class PassengerWithBaggageDto {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public Long flightId;
    public List<Baggage> baggage;
}
