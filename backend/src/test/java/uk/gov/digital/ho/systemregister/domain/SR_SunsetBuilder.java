package uk.gov.digital.ho.systemregister.domain;

import uk.gov.digital.ho.systemregister.helpers.builders.SR_SystemBuilder;

import java.time.LocalDate;

public final class SR_SunsetBuilder {
    private LocalDate date;
    private String additionalInformation;

    public static SR_SunsetBuilder aSunset() {
        return new SR_SunsetBuilder();
    }

    public SR_SunsetBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public SR_SunsetBuilder withAdditionalInformation(String additionalInfo) {
        this.additionalInformation = additionalInfo;
        return this;
    }

    public SR_Sunset build() {
        return new SR_Sunset(date, additionalInformation);
    }
}
