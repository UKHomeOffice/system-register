package uk.gov.digital.ho.systemregister.application.messaging.commands;

import java.time.Instant;
import java.util.List;

import uk.gov.digital.ho.systemregister.application.messaging.AuthoredMessage;
import uk.gov.digital.ho.systemregister.domain.SR_Person;
import uk.gov.digital.ho.systemregister.domain.SR_Risk;
import uk.gov.digital.ho.systemregister.domain.SystemData;

public class AddSystemCommand extends AuthoredMessage {
    public final SystemData systemData;

    public AddSystemCommand(SR_Person author, Instant timestamp, String name, String description,
                            String portfolio, String criticality, String investmentState, String businessOwner,
                            String serviceOwner, String technicalOwner, String productOwner,
                            String informationAssetOwner, String developedBy, String supportedBy,
                            List<String> aliases, List<SR_Risk> risks) {
        super(author, timestamp);
        systemData = new SystemData(name, description, portfolio, criticality, investmentState,
                businessOwner, serviceOwner, technicalOwner, productOwner, informationAssetOwner,
                developedBy, supportedBy, aliases, risks);
    }
}