package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

@SuppressWarnings("CdiInjectionPointsInspection")
public class SystemNameNotUniqueException extends CommandProcessingException {
    public SystemNameNotUniqueException(String name) {
        super("There is already a system called " + name);
    }
}
