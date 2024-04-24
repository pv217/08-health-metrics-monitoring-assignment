package cz.muni.fi.airportmanager.passengerservice.model.examples;

public class Examples {
    private Examples() {
    }

    public static final String VALID_PASSENGER = """
            {
                            "id": 1
                            "firstName": "John",
                            "lastName": "Doe",
                            "email": "john@gmail.com",
                            "flightId": 1,
                            "notifications": []
            }
            """;

    public static final String VALID_PASSENGER_WITH_BAGGAGE = """
            {
                            "id": 1
                            "firstName": "John",
                            "lastName": "Doe",
                            "email": "john@gmail.com",
                            "flightId": 1,
                            "notifications": [],
                            "baggage": [
                                {
                                    "id": 1,
                                    "weight": 20,
                                    "passengerId": 1,
                                    "status": "CHECKED_IN"
                                }
                            ]
            }
            """;


    public static final String VALID_PASSENGER_LIST = """
            [
                {
                    "id": 1,
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john@gmail.com",
                    "flightId": 1,
                    "notifications": [
                      {
                        "id": 1,
                        "message": "Uknown",
                        "passengerId": 1
                      },
                      {
                        "id": 2,
                        "message": "Your flight 1 has been cancelled. Reason: Unknown",
                        "passengerId": 1
                      }
                    ]
                },
                {
                    "id": 2
                    "firstName": "Jane",
                    "lastName": "Doe",
                    "email": "jane@gmail.com",
                    "flightId": 1,
                    "notifications": []
                }
            ]
            """;

    public static final String VALID_NOTIFICATION = """
            {
                "id": 1,
                "passengerId": 1,
                "message": "Notification message"
            }
            """;

    public static final String VALID_NOTIFICATION_LIST = """
            [
                {
                    "id": 1,
                    "email": "john@email.com",
                    "message": "Notification message"
                },
                {
                    "id": 2,
                    "email": "jane@email.com",
                    "message": "Notification message"
                }
            ]
            """;
}