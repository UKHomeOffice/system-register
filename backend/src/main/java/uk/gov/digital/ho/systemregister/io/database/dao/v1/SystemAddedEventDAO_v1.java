package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import java.beans.ConstructorProperties;
import java.time.Instant;

public class SystemAddedEventDAO_v1 {
    public final SystemDAO_v1 system;
    public final PersonDAO_v1 author;
    public final Instant timestamp;

    @ConstructorProperties({"system", "timestamp", "author"})
    public SystemAddedEventDAO_v1(SystemDAO_v1 system, Instant timestamp, PersonDAO_v1 author) {
        this.system = system;
        this.timestamp = timestamp;
        this.author = author;
    }
}
