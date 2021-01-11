package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.application.messaging.events.InvestmentStateUpdatedEvent;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder;

import java.time.Instant;

import static uk.gov.digital.ho.systemregister.domain.SR_PersonBuilder.aPerson;

public class InvestmentStateUpdatedEventBuilder {
    private int id = 1;
    private String investmentState = "invest";
    private SR_Person author = aPerson().build();
    private Instant timestamp = Instant.now();

    public static InvestmentStateUpdatedEventBuilder anInvestmentStateUpdatedEvent() {
        return new InvestmentStateUpdatedEventBuilder();
    }

    public InvestmentStateUpdatedEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public InvestmentStateUpdatedEventBuilder withInvestmentState(String investmentState) {
        this.investmentState = investmentState;
        return this;
    }

    public InvestmentStateUpdatedEventBuilder withAuthor(SR_PersonBuilder author) {
        this.author = author.build();
        return this;
    }

    public InvestmentStateUpdatedEventBuilder withTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }


    public InvestmentStateUpdatedEvent build() {
        return new InvestmentStateUpdatedEvent(id, investmentState, author, timestamp);
    }

}
