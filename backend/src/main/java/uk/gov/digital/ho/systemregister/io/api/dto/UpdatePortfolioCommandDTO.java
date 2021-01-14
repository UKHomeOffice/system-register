package uk.gov.digital.ho.systemregister.io.api.dto;

import uk.gov.digital.ho.systemregister.application.messaging.commands.UpdatePortfolioCommand;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public class UpdatePortfolioCommandDTO {
    public String portfolio;

    public UpdatePortfolioCommand toCommand(int id, SR_Person author, Instant timestamp) {
        return new UpdatePortfolioCommand(id, portfolio, author, timestamp);
    }
}
