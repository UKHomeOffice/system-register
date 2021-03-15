package uk.gov.digital.ho.systemregister.domain;

import java.time.LocalDate;

public class SR_Sunset {
    public LocalDate date;
    public String additionalInformation;

    @SuppressWarnings("unused")
    public SR_Sunset() {
    }

    public SR_Sunset(LocalDate date, String additionalInformation) {
        this.date = date;
        this.additionalInformation = additionalInformation;
    }

    @Override
    public String toString() {
        return "{" +
                "date=" + date +
                ", additionalInformation='" + additionalInformation + '\'' +
                '}';
    }
}
