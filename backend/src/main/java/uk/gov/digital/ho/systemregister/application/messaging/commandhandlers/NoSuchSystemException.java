package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

public class NoSuchSystemException extends Exception {
    public NoSuchSystemException(int systemId) {
        super("no such system: id " + systemId);
    }
}
