package uk.gov.digital.ho.systemregister.io.database.mappers;

import uk.gov.digital.ho.systemregister.application.messaging.events.SR_Event;
import uk.gov.digital.ho.systemregister.io.database.dao.BaseDao;

public interface DaoMapper<T extends BaseDao> {
    T mapToDao(SR_Event evt);

    <R extends SR_Event> R mapToDomain(String data);
}
