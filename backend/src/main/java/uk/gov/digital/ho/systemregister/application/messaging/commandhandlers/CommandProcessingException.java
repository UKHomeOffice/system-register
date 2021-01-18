package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

@SuppressWarnings("CdiInjectionPointsInspection")
public class CommandProcessingException extends Exception {
    public CommandProcessingException(String message) {
        super(message);
    }
}
