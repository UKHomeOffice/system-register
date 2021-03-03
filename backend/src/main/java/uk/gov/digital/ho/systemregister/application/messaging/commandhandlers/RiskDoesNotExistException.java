package uk.gov.digital.ho.systemregister.application.messaging.commandhandlers;

public class RiskDoesNotExistException extends CommandProcessingException {
    public RiskDoesNotExistException(String riskName) {
        super("system does not have risk: " + riskName);
    }
}
