package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;

public interface DAOMapper<T> {
    T map(SR_Event evt);
}
