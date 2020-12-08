package uk.gov.digital.ho.systemregister.application.messaging.events;

import org.junit.jupiter.api.Test;
import uk.gov.digital.ho.systemregister.domain.SR_System;
import uk.gov.digital.ho.systemregister.test.helpers.builders.SR_SystemBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.digital.ho.systemregister.test.helpers.builders.SR_SystemBuilder.*;

class ProductOwnerUpdatedEventTest {

    @Test
    void updatesProductOwner() {
        SR_SystemBuilder partialSystem = aSystem();
        SR_System system = partialSystem
                .withProductOwner("Mr Productface")
                .build();

        ProductOwnerUpdatedEvent event = new ProductOwnerUpdatedEvent(0, "Joe Bloggs", null, null);
        var updatedSystem = event.update(system);

        assertThat(updatedSystem).usingRecursiveComparison()
                .isEqualTo(partialSystem
                        .withProductOwner("Joe Bloggs")
                        .build());
    }
}