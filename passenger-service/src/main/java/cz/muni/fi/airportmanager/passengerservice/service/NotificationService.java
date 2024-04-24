package cz.muni.fi.airportmanager.passengerservice.service;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.model.NotificationDto;
import cz.muni.fi.airportmanager.passengerservice.repository.PassengerRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class NotificationService {

    @Inject
    PassengerRepository passengerRepository;


    /**
     * Delete all notifications
     *
     * @return number of deleted notifications
     */
    @WithTransaction
    public Uni<Long> deleteAll() {
        return Notification.deleteAll();
    }

    /**
     * Get list of all notifications
     *
     * @return list of all notifications
     */
    @WithTransaction
    public Uni<List<NotificationDto>> listAll() {
        return passengerRepository.findNotificationsWithEmail();
    }
}
