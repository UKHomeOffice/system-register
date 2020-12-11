package uk.gov.digital.ho.systemregister.helpers.builders;

import uk.gov.digital.ho.systemregister.io.api.dto.RiskDTO;
import uk.gov.digital.ho.systemregister.io.api.dto.SystemDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemDTOBuilder {
    SystemDTO sys;

    public SystemDTOBuilder() {
        sys = newSystem("name", "description", "portfolio", "high", "sunset",
                "Busy Owner", "Service Owner", "Tech Owner", "Product Owner", "Info Owner", "Home Office",
                "The others", Arrays.asList("alias 1", "alias 2"), some_risks());
    }

    public List<RiskDTO> some_risks() {
        List<RiskDTO> risks = new ArrayList() {
            {
                add(new RiskDTO("roadmap", "medium", "don't know"));
                add(new RiskDTO("sunset", "low", "not sure"));
            }
        };

        return risks;
    }

    public SystemDTO build() {
        return sys;
    }

    public SystemDTOBuilder withName(String name) {
        sys = new SystemDTO(
                name, sys.description, sys.portfolio, sys.criticality, sys.investmentState, sys.businessOwner,
                sys.serviceOwner, sys.technicalOwner, sys.productOwner, sys.informationAssetOwner, sys.developedBy,
                sys.supportedBy, sys.aliases, sys.risks);
        return this;
    }

    private SystemDTO newSystem(String name, String desc,
                                String portfolio, String criticality, String investmentState, String businessOwner,
                                String serviceOwner, String techOwner, String productOwner,
                                String informationAssetOwner, String developedBy, String supportedBy,
                                List<String> aliases, List<RiskDTO> risks) {
        return new SystemDTO(name, desc, portfolio, criticality, investmentState,
                businessOwner, serviceOwner, techOwner, productOwner, informationAssetOwner,
                developedBy, supportedBy, aliases, risks);
    }

}
