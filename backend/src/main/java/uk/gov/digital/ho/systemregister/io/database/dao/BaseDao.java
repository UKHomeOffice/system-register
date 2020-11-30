package uk.gov.digital.ho.systemregister.io.database.dao;

import java.time.Instant;

public abstract class BaseDao {
    public final Instant timestamp;

    @SuppressWarnings("CdiInjectionPointsInspection")
    protected BaseDao(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
