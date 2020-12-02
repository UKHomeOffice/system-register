package uk.gov.digital.ho.systemregister.io.database.dao.v2;

import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.List;

public class SystemAddedEventDAO_v2 extends BaseDao {
    public final System system;
    public final Person author;

    @ConstructorProperties({"system", "timestamp", "author"})
    @SuppressWarnings("CdiInjectionPointsInspection")
    public SystemAddedEventDAO_v2(System system, Instant timestamp, Person author) {
        super(timestamp);
        this.system = system;
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

    public static class System {
        public int id;
        public Instant lastUpdated;
        public String name;
        public String description;
        public String portfolio;
        public String criticality;
        public String investmentState;
        public String businessOwner;
        public String serviceOwner;
        public String technicalOwner;
        public String productOwner;
        public String informationAssetOwner;
        public String developedBy;
        public String supportedBy;
        public List<String> aliases;
        public List<Risk> risks;

        @SuppressWarnings("unused")
        public System() {
        }

        public System(int id, Instant lastUpdated, String name, String description, String portfolio,
                      String criticality, String investmentState, String businessOwner, String serviceOwner,
                      String technicalOwner, String productOwner, String informationAssetOwner, String developedBy,
                      String supportedBy, List<String> aliases, List<Risk> risks) {
            this.id = id;
            this.lastUpdated = lastUpdated;
            this.name = name;
            this.description = description;
            this.portfolio = portfolio;
            this.criticality = criticality;
            this.investmentState = investmentState;
            this.businessOwner = businessOwner;
            this.serviceOwner = serviceOwner;
            this.technicalOwner = technicalOwner;
            this.productOwner = productOwner;
            this.informationAssetOwner = informationAssetOwner;
            this.developedBy = developedBy;
            this.supportedBy = supportedBy;
            this.aliases = aliases;
            this.risks = risks;
        }
    }

    public static class Risk {
        public String name;
        public String level;
        public String rationale;

        @SuppressWarnings("unused")
        public Risk() {
        }

        public Risk(String name, String level, String rationale) {
            this.name = name;
            this.level = level;
            this.rationale = rationale;
        }
    }
}
