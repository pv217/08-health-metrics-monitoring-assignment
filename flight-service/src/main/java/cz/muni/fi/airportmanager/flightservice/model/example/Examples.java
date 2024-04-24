package cz.muni.fi.airportmanager.flightservice.model.example;


public class Examples {

    public static final String VALID_FLIGHT = """
            {
                "id": 1,
                "name": "OK 123",
                "airportFrom": "PRG",
                "airportTo": "BTS",
                "departureTime": "2021-01-01T12:00:00",
                "arrivalTime": "2021-01-01T13:00:00",
                "capacity": 100,
                "status": "SCHEDULED"
            }
            """;

    public static final String VALID_CREATE_FLIGHT = """
            {
                "name": "OK 123",
                "airportFrom": "PRG",
                "airportTo": "BTS",
                "departureTime": "2021-01-01T12:00:00",
                "arrivalTime": "2021-01-01T13:00:00",
                "capacity": 100,
                "status": "SCHEDULED"
            }
            """;

    public static final String VALID_FLIGHT_LIST = """
            [
                {
                    "id": 1,
                    "name": "OK 123",
                    "airportFrom": "PRG",
                    "airportTo": "BTS",
                    "departureTime": "2021-01-01T12:00:00",
                    "arrivalTime": "2021-01-01T13:00:00",
                    "capacity": 100,
                    "status": "SCHEDULED"
                },
                {
                    "id": 2,
                    "name": "OK 456",
                    "airportFrom": "BTS",
                    "airportTo": "PRG",
                    "departureTime": "2021-01-01T14:00:00",
                    "arrivalTime": "2021-01-01T15:00:00",
                    "capacity": 100,
                    "status": "SCHEDULED"
                }
            ]
            """;
}
