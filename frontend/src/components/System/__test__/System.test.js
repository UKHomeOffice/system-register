import React from 'react'
import { cleanup, render } from '@testing-library/react'
import { screen } from '@testing-library/dom'
import System from '../System'
import { MemoryRouter, Route } from 'react-router-dom'

//TODO mock api and remove props parameter from <System /> component

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

let _getByText = undefined
afterEach(cleanup)

describe('<System />', () => {
    describe('when the data is known', () => {

        beforeEach(() => {
            setup(test_system)
        })

        describe('System section', () => {
            it('renders system name', () => {
                const element = _getByText('System X')
                expect(element).toBeInTheDocument()
            });

            it('displays correct modified on', () => {
                const element = _getByText('Last modified: February 2020')
                expect(element).toBeInTheDocument()
            });

            it('renders description section', () => {
                const element = _getByText('Description')
                expect(element).toBeInTheDocument()
            });

            it('populates description section correctly', () => {
                const element = _getByText('The description')
                expect(element).toBeInTheDocument()
            });
        })

        describe('About section', () => {
            it('renders About section', () => {
                const element = _getByText('About')
                expect(element).toBeInTheDocument()
            });

            it('renders Aliases entry in table', () => {
                const element = _getByText('Aliases')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Aliases entry in table', () => {
                const element = _getByText('sys X, project Jupiter')
                expect(element).toBeInTheDocument()
            })

            it('renders Department entry in table', () => {
                const element = _getByText('Department')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Department entry in table', () => {
                const element = _getByText('Department of Fig and Indie')
                expect(element).toBeInTheDocument()
            })

            it('renders Criticality assessment entry in table', () => {
                const element = _getByText('Criticality assessment')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Criticality entry in table', () => {
                const element = _getByText('HIGH')
                expect(element).toBeInTheDocument()
            })

            it('renders Investment state entry in table', () => {
                const element = _getByText('Investment state')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Investment state entry in table', () => {
                const element = _getByText('EVERGREEN')
                expect(element).toBeInTheDocument()
            })

            it('renders Developed by entry in table', () => {
                const element = _getByText('Developed by')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Developed by entry in table', () => {
                const element = _getByText("Dev Team")
                expect(element).toBeInTheDocument()
            })

            it('renders Supported by entry in table', () => {
                const element = _getByText('Supported by')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Supported by entry in table', () => {
                const element = _getByText("Support Team")
                expect(element).toBeInTheDocument()
            })
        })

        describe('Contacts section', () => {
            it('renders Contacts section', () => {
                const element = _getByText('Contacts')
                expect(element).toBeInTheDocument()
            });

            it('renders System register owner entry in table', () => {
                const element = _getByText('System register owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct System register owner entry in table', () => {
                const element = _getByText('I own the register')
                expect(element).toBeInTheDocument()
            })

            it('renders Business owner entry in table', () => {
                const element = _getByText('Business owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Business owner entry in table', () => {
                const element = _getByText("I own the business")
                expect(element).toBeInTheDocument()
            })

            it('renders Technical owner entry in table', () => {
                const element = _getByText('Technical owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Technical owner entry in table', () => {
                const element = _getByText("I own the tech")
                expect(element).toBeInTheDocument()
            })

            it('renders Service owner entry in table', () => {
                const element = _getByText('Service owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Service owner entry in table', () => {
                const element = _getByText("I own the service")
                expect(element).toBeInTheDocument()
            })

            it('renders Product owner entry in table', () => {
                const element = _getByText('Product owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Product owner entry in table', () => {
                const element = _getByText("I own the product")
                expect(element).toBeInTheDocument()
            })

            it('renders Information asset owner entry in table', () => {
                const element = _getByText('Information asset owner')
                expect(element).toBeInTheDocument()
            })

            it('renders correct Information asset owner entry in table', () => {
                const element = _getByText("I own the data")
                expect(element).toBeInTheDocument()
            })
        })

        describe('Risk section', () => {
            it("renders Risk section", () => {
                const element = _getByText("Risk");
                expect(element).toBeInTheDocument();
            })

            it("displays risk name", () => {
                const element = _getByText("Roadmap");
                expect(element).toBeInTheDocument();
            })

            it("displays risk tag", () => {
                const element = _getByText("MEDIUM");
                expect(element).toBeInTheDocument();
            })

            it("has Rationale section", () => {
                const element = screen.getByTestId('risk-container');
                expect(element).toHaveTextContent("Rationale:")
            })

            it("populates Rationale section correctly", () => {
                const element = screen.getByTestId('risk-container');
                expect(element).toHaveTextContent("there is no plan")
            })
        })

    })

    describe('when the data is unknown', () => {
        beforeEach(() => setup(test_unknown_system))

        describe('System section', () => {
            it('renders system name', () => {
                const element = _getByText('System X')
                expect(element).toBeInTheDocument()
            });

            it('displays correct modified on', () => {
                const element = screen.getByTestId("system-last-modified")
                expect(element).toHaveTextContent("Never")
            });

            it('renders description section', () => {
                const element = _getByText("Description")
                expect(element).toBeInTheDocument()
            });

            it('populates description section correctly', () => {
                const element = screen.getByTestId("system-description")
                expect(element).toHaveTextContent("UNKNOWN")
            });
        })

        describe('About section', () => {
            it('renders About section', () => {
                const element = _getByText('About')
                expect(element).toBeInTheDocument()
            });

            it('renders Aliases entry in table', () => {
                const element = _getByText('Aliases')
                expect(element).toBeInTheDocument()
            })

            it("populates Aliases correctly", () => {
                const element = screen.getByTestId('about-aliases');
                expect(element).toHaveTextContent("None")
            })

            it('renders Department entry in table', () => {
                const element = _getByText('Department')
                expect(element).toBeInTheDocument()
            })

            it("populates Department correctly", () => {
                const element = screen.getByTestId('about-department');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Criticality assessment entry in table', () => {
                const element = _getByText('Criticality assessment')
                expect(element).toBeInTheDocument()
            })

            it("populates Criticality correctly", () => {
                const element = screen.getByTestId('about-criticality');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Investment state entry in table', () => {
                const element = _getByText('Investment state')
                expect(element).toBeInTheDocument()
            })

            it("populates Investment state correctly", () => {
                const element = screen.getByTestId('about-investment-state');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Developed by entry in table', () => {
                const element = _getByText('Developed by')
                expect(element).toBeInTheDocument()
            })

            it("populates Developed by correctly", () => {
                const element = screen.getByTestId('about-developed-by');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Supported by entry in table', () => {
                const element = _getByText('Supported by')
                expect(element).toBeInTheDocument()
            })

            it("populates Supported by correctly", () => {
                const element = screen.getByTestId('about-supported-by');
                expect(element).toHaveTextContent("UNKNOWN")
            })
        })

        describe('Contacts section', () => {
            it('renders Contacts section', () => {
                const element = _getByText('Contacts')
                expect(element).toBeInTheDocument()
            });

            it('renders System register owner entry in table', () => {
                const element = _getByText('System register owner')
                expect(element).toBeInTheDocument()
            })

            it("populates System register owner correctly", () => {
                const element = screen.getByTestId('contacts-system-register-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Business owner entry in table', () => {
                const element = _getByText('Business owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Business owner correctly", () => {
                const element = screen.getByTestId('contacts-business-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Technical owner entry in table', () => {
                const element = _getByText('Technical owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Technical owner correctly", () => {
                const element = screen.getByTestId('contacts-technical-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Service owner entry in table', () => {
                const element = _getByText('Service owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Service owner correctly", () => {
                const element = screen.getByTestId('contacts-service-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Product owner entry in table', () => {
                const element = _getByText('Product owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Product owner correctly", () => {
                const element = screen.getByTestId('contacts-product-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })

            it('renders Information asset owner entry in table', () => {
                const element = _getByText('Information asset owner')
                expect(element).toBeInTheDocument()
            })

            it("populates Information asset owner correctly", () => {
                const element = screen.getByTestId('contacts-information-asset-owner');
                expect(element).toHaveTextContent("UNKNOWN")
            })
        })

        describe('Risk section', () => {
            it("renders Risk section", () => {
                const element = _getByText("Risk");
                expect(element).toBeInTheDocument();
            })

            it("displays risk name", () => {
                const element = _getByText("Roadmap");
                expect(element).toBeInTheDocument();
            })

            //TODO: Maybe look at this test again
            it("has correct risk tag", () => {
                const element = screen.getAllByTestId('risk-risk-badge-null');
                expect(element[0]).toHaveTextContent("UNKNOWN");
            })

            it("has Rationale section", () => {
                const element = screen.getByTestId('risk-container');
                expect(element).toHaveTextContent("Rationale:");
            })

            it('populates Rationale section correctly', () => {
                const element = screen.getByTestId('risk-rationale-roadmap');
                expect(element).toHaveTextContent("UNKNOWN");
            })
        })

    })
});

function setup(test_data) {
    const { getByText } = render(
        <MemoryRouter initialEntries={['system/1']}>
            <Route path='system/:id'>
                <System system={test_data} />
            </Route>
        </MemoryRouter>)
    _getByText = getByText
}
