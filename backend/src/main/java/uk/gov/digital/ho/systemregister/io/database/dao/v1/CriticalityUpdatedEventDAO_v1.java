package uk.gov.digital.ho.systemregister.io.database.dao.v1;

import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;

import java.beans.ConstructorProperties;
import java.time.Instant;

public class CriticalityUpdatedEventDAO_v1 extends BaseDao {
    public final int id;
    public final String criticality;
    public final Person author;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @ConstructorProperties({"id", "criticality", "timestamp", "author"})
    public CriticalityUpdatedEventDAO_v1(int id, String criticality, Instant timestamp, Person author) {
        super(timestamp);
        this.id = id;
        this.criticality = criticality;
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




