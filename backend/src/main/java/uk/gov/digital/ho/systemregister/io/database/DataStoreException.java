package uk.gov.digital.ho.systemregister.io.database;

@SuppressWarnings("CdiInjectionPointsInspection")
public class DataStoreException extends Exception {
    DataStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
