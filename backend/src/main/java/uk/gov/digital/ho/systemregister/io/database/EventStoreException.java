package uk.gov.digital.ho.systemregister.io.database;

@SuppressWarnings("CdiInjectionPointsInspection")
public class EventStoreException extends Exception {
    EventStoreException(String message) {
        super(message);
    }

    EventStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
