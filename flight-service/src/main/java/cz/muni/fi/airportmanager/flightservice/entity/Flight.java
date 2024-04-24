package cz.muni.fi.airportmanager.flightservice.entity;


import cz.muni.fi.airportmanager.flightservice.model.CreateFlightDto;
import cz.muni.fi.airportmanager.flightservice.model.FlightDto;
import cz.muni.fi.airportmanager.flightservice.model.FlightStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Flight {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String airportFrom;
    private String airportTo;
    private Date departureTime;
    private Date arrivalTime;
    private int capacity;
    private FlightStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(String airportFrom) {
        this.airportFrom = airportFrom;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(String airportTo) {
        this.airportTo = airportTo;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public FlightDto toDto() {
        FlightDto flightDto = new FlightDto();
        flightDto.id = id;
        flightDto.name = name;
        flightDto.airportFrom = airportFrom;
        flightDto.airportTo = airportTo;
        flightDto.departureTime = departureTime;
        flightDto.arrivalTime = arrivalTime;
        flightDto.capacity = capacity;
        flightDto.status = status;
        return flightDto;
    }

    public static Flight fromDto(CreateFlightDto createFlightDto) {
        Flight flight = new Flight();
        flight.name = createFlightDto.name;
        flight.airportFrom = createFlightDto.airportFrom;
        flight.airportTo = createFlightDto.airportTo;
        flight.departureTime = createFlightDto.departureTime;
        flight.arrivalTime = createFlightDto.arrivalTime;
        flight.capacity = createFlightDto.capacity;
        flight.status = createFlightDto.status;
        return flight;
    }

    public static Flight fromDto(FlightDto flightDto) {
        Flight flight = new Flight();
        flight.id = flightDto.id;
        flight.name = flightDto.name;
        flight.airportFrom = flightDto.airportFrom;
        flight.airportTo = flightDto.airportTo;
        flight.departureTime = flightDto.departureTime;
        flight.arrivalTime = flightDto.arrivalTime;
        flight.capacity = flightDto.capacity;
        flight.status = flightDto.status;
        return flight;
    }
}