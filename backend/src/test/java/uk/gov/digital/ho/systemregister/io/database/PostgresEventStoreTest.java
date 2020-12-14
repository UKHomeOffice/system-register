package uk.gov.digital.ho.systemregister.io.database;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.helpers.builders.SystemAddedEventBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;
import static uk.gov.digital.ho.systemregister.domain.SR_RiskBuilder.aHighRisk;
import static uk.gov.digital.ho.systemregister.domain.SR_RiskBuilder.aLowRisk;
import static uk.gov.digital.ho.systemregister.helpers.builders.ProductOwnerUpdatedEventBuilder.aProductOwnerUpdatedEvent;
import static uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder.aSystem;
import static uk.gov.digital.ho.systemregister.helpers.builders.SystemAddedEventBuilder.aSystemAddedEvent;
import static uk.gov.digital.ho.systemregister.helpers.builders.UpdateCriticalityCommandBuilder.aCriticalityUpdatedEvent;
import static uk.gov.digital.ho.systemregister.util.ResourceUtils.getResourceAsString;

@QuarkusTest
@DisabledIfEnvironmentVariable(named = "CI", matches = "drone")
public class PostgresEventStoreTest {
    @Inject
    PostgresEventStore eventStore;

    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    AgroalDataSource dataSource;

    @BeforeEach
    void cleanUpEventStore() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE eventstore.snapshots, eventstore.events;");
        }
    }

    @Test
    public void getSnapshot_Empty() {
        var actual = eventStore.getSnapshot();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void saveSnapshot() {
        var expected = new SnapshotBuilder().withASystem().build();
        eventStore.save(expected);

        var actual = eventStore.getSnapshot();

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void saveLargeSnapshot() {
        var expected = new SnapshotBuilder().withManySystems(5000).build();
        eventStore.save(expected);

        var actual = eventStore.getSnapshot();

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    public void saveSystemAddedEvent() {
        var expected = new SystemAddedEventBuilder().build();
        eventStore.save(expected);

        var actual = eventStore.getEvents();

        assertTrue(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison()
                .isEqualTo(List.of(expected));
    }

    @Test
    public void saveProductOwnerUpdatedEvent() {
        var expected = aProductOwnerUpdatedEvent()
                .build();
        eventStore.save(expected);

        var actual = eventStore.getEvents();

        assertTrue(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison()
                .isEqualTo(List.of(expected));
    }

    @Test
    public void saveCriticalityUpdated() {
        var expected = aCriticalityUpdatedEvent()
                .build();
        eventStore.save(expected);

        var actual = eventStore.getEvents();

        assertTrue(actual.isPresent());
        assertThat(actual.get()).usingRecursiveComparison()
                .isEqualTo(List.of(expected));
    }


    @Test
    public void throwsExceptionIfEventTypeNotSupportedWhenSaving() {
        var event = new SR_Event(aPerson().build(), Instant.now());

        assertThatThrownBy(() -> eventStore.save(event))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageMatching(".*not supported: .*\\.SR_Event Please implement a DAO Mapper for this event type$");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "uk.gov.digital.ho.systemregister.application.messaging.events.SystemAddedEvent",
            "uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemAddedEventDAO_v1",
    })
    public void canReadOlderSystemAddedEvents(String objectType) {
        var expectedEvent = aSystemAddedEvent()
                .withAuthor(aPerson().withUsername("corey"))
                .withTimeStamp("2020-11-30T13:29:44.886532Z")
                .withSystem(aSystem()
                        .withId(1)
                        .withName("Newly added system")
                        .withAliases("noob")
                        .withDescription("A test system for testing purposes in testing times")
                        .withDevelopedBy("ThoughtWorks")
                        .withRisks(
                                aLowRisk().withName("badger").withRationale("Deadly badger attacks unlikely"),
                                aHighRisk().withName("roadmap").withRationale("Problems about to happen"))
                        .withLastUpdated("2020-11-30T13:29:44.883847Z")
                        .withTechnicalOwner(null)
                        .withServiceOwner(null)
                        .withSupportedBy(null))
                .build();
        insertEvent(0, objectType, getResourceAsString("dao/v1/encrypted-system-added-event.txt"));

        var maybeEvents = eventStore.getEvents();

        assertThat(maybeEvents).hasValueSatisfying(events ->
                assertThat(events).usingRecursiveComparison()
                        .isEqualTo(List.of(expectedEvent)));
    }

    @Test
    public void canReadNewerSystemAddedEvents() {
        var expectedEvent = aSystemAddedEvent()
                .withAuthor(aPerson()
                        .withUsername("user")
                        .withFirstName("forename")
                        .withSurname("surname")
                        .withEmail("email"))
                .withTimeStamp("2020-10-09T08:07:06.050Z")
                .withSystem(aSystem()
                        .withId(1234)
                        .withName("system")
                        .withAliases("systems register", "systems audit", "system audit")
                        .withDescription("Central source of system names, contacts and risk information")
                        .withDevelopedBy("Developers")
                        .withRisks(aLowRisk()
                                .withName("change")
                                .withRationale("Designed to be easy to change"))
                        .withLastUpdated("2020-10-09T08:07:05.000Z")
                        .withTechnicalOwner("Techy Owner")
                        .withServiceOwner("Service Owner")
                        .withSupportedBy("All"))
                .build();
        insertEvent(
                1,
                "uk.gov.digital.ho.systemregister.io.database.dao.v2.SystemAddedEventDAO_v2",
                getResourceAsString("dao/v2/encrypted-system-added-event.txt"));

        var maybeEvents = eventStore.getEvents();

        assertThat(maybeEvents).hasValueSatisfying(events ->
                assertThat(events).usingRecursiveComparison()
                        .isEqualTo(List.of(expectedEvent)));
    }

    @SuppressWarnings("SameParameterValue")
    private void insertEvent(int id, String type, String data) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("INSERT INTO eventstore.events (id, time_stamp, object_type, object_data) VALUES (?, ?, ?, decode(?, 'base64'));")
        ) {
            statement.setLong(1, id);
            statement.setTimestamp(2, Timestamp.from(Instant.now()));
            statement.setString(3, type);
            statement.setString(4, data);
            statement.execute();
        } catch (SQLException e) {
            throw new AssertionError("unable to write to database", e);
        }
    }
}
