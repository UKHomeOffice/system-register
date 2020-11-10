package uk.gov.digital.ho.systemregister.io.database;

public class EventStoreException extends Exception {
    /**
     *v1.0.0
     */
    private static final long serialVersionUID = 1L;

    public EventStoreException(String message, Throwable err) {
        super(message, err);
    }
}
