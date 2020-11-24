import React from 'react'
import { cleanup, render } from '@testing-library/react'
import { screen } from '@testing-library/dom'
import System from '../System'
import { MemoryRouter, Route } from 'react-router-dom'

import api from '../../../services/api';

jest.mock('../../../services/api', () => ({
    getSystem: jest.fn(),
}))

const test_system = {
    id: 444,
    name: "System X",
    aliases: ["sys X", "project Jupiter"],
    department: "Department of Fig and Indie",
    criticality: "high",
    investment_state: "evergreen",
    description: "The description",
    portfolio: 'Portfolio Y',
    last_updated: '2020-02-13T11:01:01',
    system_register_owner: "I own the register",
    business_owner: "I own the business",
    service_owner: "I own the service",
    technical_owner: "I own the tech",
    product_owner: "I own the product",
    information_asset_owner: "I own the data",
    developed_by: "Dev Team",
    supported_by: "Support Team",
    risks: [
        {
            "name": "roadmap",
            "level": "medium",
            "rationale": "there is no plan"
        },
        {
            "name": "tech_stack",
            "level": "low",
            "rationale": "Really good tech"
        }
    ]
}

const test_unknown_system = {
    id: 444,
    name: "System X",
    aliases: [],
    criticality: "",
    investment_state: "",
    description: "",
    portfolio: '',
    last_updated: '',
    business_owner: "",
    service_owner: "",
    technical_owner: "",
    product_owner: "",
    information_asset_owner: "",
    developed_by: "",
    supported_by: "",
    risks: [
        {
            "name": "roadmap",
            "level": null,
            "rationale": null
        },
        {
            "name": "change",
            "level": null,
            "rationale": null
        }
    ]
}

describe('<System />', () => {
    afterEach(cleanup)

    describe('when the data is known', () => {
        beforeEach(() => {
            api.getSystem.mockResolvedValue(test_system);
        })

        describe('System section', () => {
            it('renders system name', async () => {
                setup();
                const element = await screen.findByText('System X')
                expect(element).toBeInTheDocument()
            });

            it('displays correct modified on', async () => {
                setup();
                const element = await screen.findByText('Last modified: 13 February 2020')
                expect(element).toBeInTheDocument()
            });

            it('renders description section', async () => {
                setup();
                const element = await screen.findByText('Description')
                expect(element).toBeInTheDocument()
            });

            it('populates description section correctly', async () => {
                setup();
                const element = await screen.findByText('The description')
                expect(element).toBeInTheDocument()
            });
        })

        describe('About section', () => {
            it('renders About section', async () => {
                setup();
                const element = await screen.findByText('About')
                expect(element).toBeInTheDocument()
            });

            it('renders Aliases entry in table', async () => {
                setup();
                const element = await screen.findByText('Aliases')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Aliases entry in table', async () => {
                setup();
                const element = await screen.findByText('sys X, project Jupiter')
                expect(element).toBeInTheDocument()
            })

            it('renders Department entry in table', async () => {
                setup();
                const element = await screen.findByText('Department')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Department entry in table', async () => {
                setup();
                const element = await screen.findByText('Department of Fig and Indie')
                expect(element).toBeInTheDocument()
            })

            it('renders Criticality assessment entry in table', async () => {
                setup();
                const element = await screen.findByText('Criticality assessment')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Criticality entry in table', async () => {
                setup();
                const element = await screen.findByText('HIGH')
                expect(element).toBeInTheDocument()
            })

            it('renders Investment state entry in table', async () => {
                setup();
                const element = await screen.findByText('Investment state')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Investment state entry in table', async () => {
                setup();
                const element = await screen.findByText('EVERGREEN')
                expect(element).toBeInTheDocument()
            })

            it('renders Developed by entry in table', async () => {
                setup();
                const element = await screen.findByText('Developed by')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Developed by entry in table', async () => {
                setup();
                const element = await screen.findByText("Dev Team")
                expect(element).toBeInTheDocument()
            })

            it('renders Supported by entry in table', async () => {
                setup();
                const element = await screen.findByText('Supported by')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Supported by entry in table', async () => {
                setup();
                const element = await screen.findByText("Support Team")
                expect(element).toBeInTheDocument()
            })
        })

        describe('Contacts section', () => {
            it('renders Contacts section', async () => {
                setup();
                const element = await screen.findByText('Contacts')
                expect(element).toBeInTheDocument()
            });

            it('renders System register owner entry in table', async () => {
                setup();
                const element = await screen.findByText('System register owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct System register owner entry in table', async () => {
                setup();
                const element = await screen.findByText('I own the register')
                expect(element).toBeInTheDocument()
            })

            it('renders Business owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Business owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Business owner entry in table', async () => {
                setup();
                const element = await screen.findByText("I own the business")
                expect(element).toBeInTheDocument()
            })

            it('renders Technical owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Technical owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Technical owner entry in table', async () => {
                setup();
                const element = await screen.findByText("I own the tech")
                expect(element).toBeInTheDocument()
            })

            it('renders Service owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Service owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Service owner entry in table', async () => {
                setup();
                const element = await screen.findByText("I own the service")
                expect(element).toBeInTheDocument()
            })

            it('renders Product owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Product owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Product owner entry in table', async () => {
                setup();
                const element = await screen.findByText("I own the product")
                expect(element).toBeInTheDocument()
            })

            it('renders Information asset owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Information asset owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Information asset owner entry in table', async () => {
                setup();
                const element = await screen.findByText("I own the data")
                expect(element).toBeInTheDocument()
            })
        })

        describe('Risk section', () => {
            it("renders Risk section", async () => {
                setup();
                const element = await screen.findByText("Risk");
                expect(element).toBeInTheDocument();
            })

            it("displays risk name", async () => {
                setup();
                const element = await screen.findByText("Roadmap");
                expect(element).toBeInTheDocument();
            })

            it("displays risk tag", async () => {
                setup();
                const element = await screen.findByText("MEDIUM");
                expect(element).toBeInTheDocument();
            })

            it("has Rationale section", async () => {
                setup();
                const element = await screen.findByTestId('risk-container');
                expect(element).toHaveTextContent("Rationale:")
            })

            it("populates Rationale section correctly", async () => {
                setup();
                const element = await screen.findByTestId('risk-container');
                expect(element).toHaveTextContent("there is no plan")
            })
        })

    })

    describe('when the data is unknown', () => {
        beforeEach(() => {
            api.getSystem.mockResolvedValue(test_unknown_system);
        })

        describe('System section', () => {
            it('renders system name', async () => {
                setup();
                const element = await screen.findByText('System X')
                expect(element).toBeInTheDocument()
            });

            it('displays correct modified on', async () => {
                setup();
                const element = await screen.findByTestId("system-last-modified")
                expect(element).toHaveTextContent("Never")
            });

            it('renders description section', async () => {
                setup();
                const element = await screen.findByText("Description")
                expect(element).toBeInTheDocument()
            });

            it('populates description section correctly', async () => {
                setup();
                const element = await screen.findByTestId("system-description")
                expect(element).toHaveTextContent("UNKNOWN")
            });
        })

        describe('About section', () => {
            it('renders About section', async () => {
                setup();
                const element = await screen.findByText('About')
                expect(element).toBeInTheDocument()
            });

            it('renders Aliases entry in table', async () => {
                setup();
                const element = await screen.findByText('Aliases')
                expect(element).toBeInTheDocument()
            })

            it("populates Aliases correctly", async () => {
                setup();
                const element = await screen.findByTestId('about-aliases');
                expect(element).toHaveTextContent("None")
            })

            it('renders Department entry in table', async () => {
                setup();
                const element = await screen.findByText('Department')
                expect(element).toBeInTheDocument()
            })

            it("populates Department correctly", async () => {
                setup();
                const element = await screen.findByTestId('about-department');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Criticality assessment entry in table', async () => {
                setup();
                const element = await screen.findByText('Criticality assessment')
                expect(element).toBeInTheDocument()
            })

            it("populates Criticality correctly", async () => {
                setup();
                const element = await screen.findByTestId('about-criticality');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Investment state entry in table', async () => {
                setup();
                const element = await screen.findByText('Investment state')
                expect(element).toBeInTheDocument()
            })

            it("populates Investment state correctly", async () => {
                setup();
                const element = await screen.findByTestId('about-investment-state');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Developed by entry in table', async () => {
                setup();
                const element = await screen.findByText('Developed by')
                expect(element).toBeInTheDocument()
            })

            it("populates Developed by correctly", async () => {
                setup();
                const element = await screen.findByTestId('about-developed-by');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Supported by entry in table', async () => {
                setup();
                const element = await screen.findByText('Supported by')
                expect(element).toBeInTheDocument()
            })

            it("populates Supported by correctly", async () => {
                setup();
                const element = await screen.findByTestId('about-supported-by');
                expect(element).toHaveTextContent("UNKNOWN")
            })
        })

        describe('Contacts section', () => {
            it('renders Contacts section', async () => {
                setup();
                const element = await screen.findByText('Contacts')
                expect(element).toBeInTheDocument()
            });

            it('renders System register owner entry in table', async () => {
                setup();
                const element = await screen.findByText('System register owner')
                expect(element).toBeInTheDocument()
            })

            it("populates System register owner correctly", async () => {
                setup();
                const element = await screen.findByTestId('contacts-system-register-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Business owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Business owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Business owner correctly", async () => {
                setup();
                const element = await screen.findByTestId('contacts-business-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Technical owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Technical owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Technical owner correctly", async () => {
                setup();
                const element = await screen.findByTestId('contacts-technical-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Service owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Service owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Service owner correctly", async () => {
                setup();
                const element = await screen.findByTestId('contacts-service-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Product owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Product owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Product owner correctly", async () => {
                setup();
                const element = await screen.findByTestId('contacts-product-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Information asset owner entry in table', async () => {
                setup();
                const element = await screen.findByText('Information asset owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Information asset owner correctly", async () => {
                setup();
                const element = await screen.findByTestId('contacts-information-asset-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })
        })

        describe('Risk section', () => {
            it("renders Risk section", async () => {
                setup();
                const element = await screen.findByText("Risk");
                expect(element).toBeInTheDocument();
            })

            it("renders the details of each risk", async () => {
                setup();
                const risks = await screen.findAllByTestId('risk-details');
                expect(risks).toHaveLength(2);
            })
        })
    })
});

function setup() {
    render(
      <MemoryRouter initialEntries={['system/1']}>
          <Route path='system/:id'>
              <System />
          </Route>
      </MemoryRouter>)
}
