package uk.gov.digital.ho.systemregister.domain;

public class AddSystemResult {
    public final SR_System system;
    public final Change result;

    public AddSystemResult(SR_System item, Change result) {
        this.system = item;
        this.result = result;
    }

    public static AddSystemResult added(SR_System newSystem) {
        return new AddSystemResult(newSystem, Change.ADDED);
    }

    public static AddSystemResult duplicate(SR_System newSystem) {
      return new AddSystemResult(newSystem, Change.DUPLICATE);
    }
}
