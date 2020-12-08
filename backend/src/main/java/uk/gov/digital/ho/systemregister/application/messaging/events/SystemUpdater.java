package uk.gov.digital.ho.systemregister.application.messaging.events;

import uk.gov.digital.ho.systemregister.domain.SR_System;

public interface SystemUpdater {

    SR_System update(SR_System system);

}
