package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

@SuppressWarnings("CdiInjectionPointsInspection")
public class CommandHasNoEffectException extends CommandProcessingException {
    public CommandHasNoEffectException(String message) {
        super(message);
    }
}
