package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.application.messaging.AuthoredMessage;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

import java.time.Instant;

public class SR_Event extends AuthoredMessage {

    public SR_Event() {
    }

    public SR_Event(SR_Person author, Instant timestamp) {
        super(author, timestamp);
    }
}
