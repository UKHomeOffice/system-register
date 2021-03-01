package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.List;

public class SystemRisksUpdatedEventDAO_v1 extends BaseDao {
    public final int id;
    public final SystemRisksUpdatedEventDAO_v1.Person author;
    public List<SR_Risk> risks;

    @ConstructorProperties({"id", "timestamp", "author"})
    @SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
    public SystemRisksUpdatedEventDAO_v1(int id, Instant timestamp, SystemRisksUpdatedEventDAO_v1.Person author) {
        this(id, null, timestamp, author);
    }

    public SystemRisksUpdatedEventDAO_v1(int id, List<SR_Risk> risks, Instant timestamp, SystemRisksUpdatedEventDAO_v1.Person author) {
        super(timestamp);
        this.id = id;
        this.risks = risks;
        this.author = author;
    }

    public static class Person {
        public String username;
        public String firstName;
        public String surname;
        public String email;

        @SuppressWarnings("unused")
        public Person() {
        }

        public Person(String username, String firstName, String surname, String email) {
            this.username = username;
            this.firstName = firstName;
            this.surname = surname;
            this.email = email;
        }
    }
}
