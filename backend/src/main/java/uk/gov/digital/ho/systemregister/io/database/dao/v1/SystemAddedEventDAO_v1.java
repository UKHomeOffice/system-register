package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;

import java.beans.ConstructorProperties;
import java.time.Instant;

public class SystemAddedEventDAO_v1 extends BaseDao {
    public final SystemDAO_v1 system;
    public final PersonDAO_v1 author;

    @ConstructorProperties({"system", "timestamp", "author"})
    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemAddedEventDAO_v1(SystemDAO_v1 system, Instant timestamp, PersonDAO_v1 author) {
        super(timestamp);
        this.system = system;
        this.author = author;
    }
}
