package uk.gov.digital.ho.systemregister.io.api.dto;


import java.time.Instant;
import java.util.List;

public class SnapshotDTO {
    public List<RegisteredSystemDTO> systems;
    public Instant timestamp;

    public SnapshotDTO() {
    }

    public SnapshotDTO(List<RegisteredSystemDTO> systems, Instant timestamp) {
        this.systems = systems;
        this.timestamp = timestamp;
    }
}
