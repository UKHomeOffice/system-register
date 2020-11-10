package uk.gov.digital.ho.systemregister.application.messaging;

import java.time.Instant;
import uk.gov.digital.ho.systemregister.domain.SR_Person;

public abstract class AuthoredMessage { //todo really would like immutability here :(
    public SR_Person author;
    public Instant timestamp;

    public AuthoredMessage() {
    }

    public AuthoredMessage(SR_Person author, Instant timestamp) {
        this.author = author;
        this.timestamp = timestamp;
    }    

}
