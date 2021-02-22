package uk.gov.digital.ho.systemregister.domain;

@SuppressWarnings("CdiInjectionPointsInspection")
public class DuplicateSystemException extends Exception {
    private final SystemData system;

    public DuplicateSystemException(SystemData system) {
        this.system = system;
    }

    public SystemData getSystem() {
        return system;
    }
}
