package uk.gov.digital.ho.systemregister.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class SystemRegister {
    private final Map<String, SR_System> systems;

    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemRegister(List<SR_System> systems) {
        this.systems = systems.stream()
                .collect(toMap(
                        system -> system.name,
                        identity()));
    }

    public Optional<SR_System> getSystemById(int id) {
        return systems.values().stream()
                .filter(system -> system.id == id)
                .findFirst();
    }

    public SR_System addSystem(SystemData system) throws DuplicateSystemException {
        if (systems.containsKey(system.name)) {
            throw new DuplicateSystemException(system);
        }

        SR_System newSystem = buildSystem(system);
        systems.put(newSystem.name, newSystem);
        return newSystem;
    }

    public List<SR_System> getAllSystems() {
        return List.copyOf(systems.values());
    }

    private SR_System buildSystem(SystemData sys) {
        return new SR_System(getId(), sys.name, sys.description, Instant.now(), sys.portfolio, sys.criticality, sys.investmentState, sys.businessOwner,
                sys.serviceOwner, sys.technicalOwner, sys.productOwner, sys.informationAssetOwner, sys.developedBy,
                sys.supportedBy, sys.aliases, sys.risks, new SR_Sunset(null, null), null);
    }

    private int getId() {
        return systems.values().stream()
                .mapToInt(s -> s.id)
                .max()
                .orElse(0) + 1;
    }
}
