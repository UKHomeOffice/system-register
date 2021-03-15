package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import uk.gov.digital.ho.systemregister.domain.SR_Sunset;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;

import java.beans.ConstructorProperties;
import java.time.Instant;

public class SunsetUpdatedEventDAO_v1 extends BaseDao {
    public final int id;
    public final SunsetUpdatedEventDAO_v1.Person author;
    public SR_Sunset sunset;

    @ConstructorProperties({"id", "timestamp", "author"})
    @SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
    public SunsetUpdatedEventDAO_v1(int id, Instant timestamp, SunsetUpdatedEventDAO_v1.Person author) {
        this(id, null, timestamp, author);
    }

    public SunsetUpdatedEventDAO_v1(int id, SR_Sunset sunset, Instant timestamp, SunsetUpdatedEventDAO_v1.Person author) {
        super(timestamp);
        this.id = id;
        this.sunset = sunset;
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
