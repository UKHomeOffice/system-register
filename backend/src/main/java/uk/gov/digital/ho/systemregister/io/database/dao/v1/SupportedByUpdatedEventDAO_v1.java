package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;

import java.beans.ConstructorProperties;
import java.time.Instant;

public class SupportedByUpdatedEventDAO_v1 extends BaseDao {
    public final int id;
    public final SupportedByUpdatedEventDAO_v1.Person author;
    public String supportedBy;

    @ConstructorProperties({"id", "timestamp", "author"})
    @SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
    public SupportedByUpdatedEventDAO_v1(int id, Instant timestamp, SupportedByUpdatedEventDAO_v1.Person author) {
        this(id, null, timestamp, author);
    }

    public SupportedByUpdatedEventDAO_v1(int id, String supportedBy, Instant timestamp, SupportedByUpdatedEventDAO_v1.Person author) {
        super(timestamp);
        this.id = id;
        this.supportedBy = supportedBy;
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
