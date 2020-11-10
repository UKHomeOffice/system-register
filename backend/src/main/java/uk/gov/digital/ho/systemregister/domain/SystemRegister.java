package uk.gov.digital.ho.systemregister.domain;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class SystemRegister {
    private final Dictionary<String, SR_System> systems;

    public SystemRegister(List<SR_System> systems) {
        this.systems = systems.stream().collect(Collectors.toMap(
                s -> s.name, s -> s,
                (u, v) -> {
                    throw new IllegalStateException(
                            String.format("Cannot have 2 values (%s, %s) for the same key", u, v)
                    );
                }, Hashtable::new
        ));
    }

    public AddSystemResult addSystem(SystemData system) {
        if (systems.get(system.name) != null) {
            return AddSystemResult.Duplicate(systems.get(system.name));
        }
        SR_System newSystem = buildSystem(system);
        systems.put(newSystem.name, newSystem);
        return AddSystemResult.Added(newSystem);
    }

    public List<SR_System> getAllSysytems() {
        return Collections.list(systems.elements());
    }

    private SR_System buildSystem(SystemData sys) {
        return new SR_System(getId(), sys.name, sys.description, Instant.now(),
                sys.portfolio, sys.criticality, sys.investmentState, sys.businessOwner,
                sys.serviceOwner, sys.technicalOwner, sys.productOwner, sys.informationAssetOwner,
                sys.developedBy, sys.supportedBy, sys.aliases, sys.risks);
    }

    private int getId() {
        if (systems.size() == 0) {
            return 1;
        }
        return Collections.list(systems.elements()).stream().map(s -> s.id).max(Integer::compare).get() + 1;
    }
}
