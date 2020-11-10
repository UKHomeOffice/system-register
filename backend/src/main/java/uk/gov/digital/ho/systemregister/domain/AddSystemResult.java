package uk.gov.digital.ho.systemregister.domain;

public class AddSystemResult {
    public final SR_System system;
    public final CHANGE result;

    public AddSystemResult(SR_System item, CHANGE result) {
        this.system = item;
        this.result = result;
    }

    public static AddSystemResult Added(SR_System newSystem) {
        return new AddSystemResult(newSystem, CHANGE.ADDED);
    }

    public static AddSystemResult Duplicate(SR_System newSystem) {
      return new AddSystemResult(newSystem, CHANGE.DUPLICATE);
    }
}
