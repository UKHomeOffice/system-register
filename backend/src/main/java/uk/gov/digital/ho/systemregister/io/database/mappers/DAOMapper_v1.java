package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.database.dao.v1.SystemAddedEventDAO_v1;

import javax.inject.Named;

@Named("v1")
public class DAOMapper_v1 implements DAOMapper<SystemAddedEventDAO_v1> {
    @Override
    public SystemAddedEventDAO_v1 map(SR_Event evt) {
        throw new UnsupportedOperationException();
    }
}
