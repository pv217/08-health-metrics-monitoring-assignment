package cz.muni.fi.airportmanager.baggageservice.model.example;


public class Examples {
    private Examples() {
    }

    public static final String VALID_BAGGAGE = """
            {
                "id": 1,
                "weight": 20.0,
                "passengerId": 1,
                "status": "CHECKED_IN"
            }
            """;

    public static final String VALID_BAGGAGE_LIST = """
            [
                {
                    "id": 1,
                    "weight": 20.0,
                    "passengerId": 1,
                    "status": "CHECKED_IN"
                },
                {
                    "id": 2,
                    "weight": 30.0,
                    "passengerId": 2,
                    "status": "LOST"
                }
            ]
            """;
}
