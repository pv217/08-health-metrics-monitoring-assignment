package cz.muni.fi.airportmanager.passengerservice.model;

public class NotificationDto {
    public Long id;
    public String message;
    public String email;

    public NotificationDto(Long id, String message, String email) {
        this.id = id;
        this.message = message;
        this.email = email;
    }

    public NotificationDto() {
    }
}
