package uk.gov.digital.ho.systemregister.io.api.dto;

import javax.json.bind.annotation.JsonbProperty;

public class UpdateInvestmentStateCommandDTO {
    @JsonbProperty("investment_state")
    public String investmentState;
}