package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.PortfolioUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class PortfolioUpdatedEventBuilder {
    private int id = 1;
    private String portfolio = "option 1";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static PortfolioUpdatedEventBuilder aPortfolioUpdatedEvent() {
        return new PortfolioUpdatedEventBuilder();
    }

    public PortfolioUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public PortfolioUpdatedEventBuilder withPortfolio(String portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public PortfolioUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public PortfolioUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public PortfolioUpdatedEvent build() {
        return new PortfolioUpdatedEvent(id, portfolio, author, timestamp);
    }

}
