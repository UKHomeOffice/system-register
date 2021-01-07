package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

public class SystemNameNotUniqueException extends Exception {
    public SystemNameNotUniqueException(String name) {
        super("A system with that name already exists: " + name);
    }
}
