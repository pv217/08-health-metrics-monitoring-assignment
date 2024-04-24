package cz.muni.fi.airportmanager.passengerservice.grpc;

import cz.muni.fi.airportmanager.passengerservice.entity.Notification;
import cz.muni.fi.airportmanager.passengerservice.service.PassengerService;
import cz.muni.fi.airportmanager.proto.FlightCancellationRequest;
import cz.muni.fi.airportmanager.proto.FlightCancellationResponse;
import cz.muni.fi.airportmanager.proto.FlightCancellationResponseStatus;
import cz.muni.fi.airportmanager.proto.MutinyFlightCancellationGrpc;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;


@GrpcService
public class FlightCancellationService extends MutinyFlightCancellationGrpc.FlightCancellationImplBase {


    @Inject
    PassengerService passengerService;

    /**
     * Cancel flight and add notification for all passengers
     *
     * @param request request with flight id and reason for cancellation
     * @return response with status of cancellation
     */
    @Override
    public Uni<FlightCancellationResponse> cancelFlight(FlightCancellationRequest request) {
        var flightId = request.getId();
        var reason = request.getReason();
        var notification = new Notification();
        notification.message = "Your flight " + flightId + " has been cancelled. Reason: " + reason;
        return passengerService.addNotificationByFlightId((long) flightId, notification)
                .onItem().transform(ignored -> FlightCancellationResponse.newBuilder().setStatus(FlightCancellationResponseStatus.Cancelled).build());
    }
}
