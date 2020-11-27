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
};

const test_unknown_system = {
    id: 444,
    name: "System Y",
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
};

describe('<System />', () => {
    afterEach(cleanup)

    describe('when the data is known', () => {
        beforeEach(() => {
            api.getSystem.mockResolvedValue(test_system);
        });

        describe('System section', () => {
            it('renders system name', async () => {
                setup();
                const element = await screen.findByText('System X');
                expect(element).toBeInTheDocument();
            });

            it('displays correct modified on', async () => {
                setup();
                const element = await screen.findByText('Last modified: 13 February 2020');
                expect(element).toBeInTheDocument();
            });

            it('renders Description section', async () => {
                setup();
                const element = await screen.findByRole("heading", {name: "Description"});
                expect(element).toBeInTheDocument();
            });

        });

        describe('About section', () => {
            it('renders About section', async () => {
                setup();
                const element = await screen.findByRole("heading", {name: "About"});
                expect(element).toBeInTheDocument();
            });

            it('renders first column correclty', async () => {
                setup();
                const column = await screen.findByTestId("about-column1");
                const entries = column.childNodes;
                expect(entries[0].textContent).toEqual("Aliases");
                expect(entries[1].textContent).toEqual("Department");
                expect(entries[2].textContent).toEqual("Criticality assessment");
                expect(entries[3].textContent).toEqual("Investment state");
                expect(entries[4].textContent).toEqual("Developed by");
                expect(entries[5].textContent).toEqual("Supported by");
            });

            it('renders correct entries in the second column of About table', async () => {
                setup();
                const column = await screen.findByTestId("about-column2");
                const entries = column.childNodes;
                expect(entries[0].textContent).toEqual("sys X, project Jupiter");
                expect(entries[1].textContent).toEqual("Department of Fig and Indie");
                expect(entries[2].textContent).toEqual("HIGH");
                expect(entries[3].textContent).toEqual("EVERGREEN");
                expect(entries[4].textContent).toEqual("Dev Team");
                expect(entries[5].textContent).toEqual("Support Team");
            })
      });

        describe('Contacts section', () => {
            it('renders Contacts section', async () => {
                setup();
                const element = await screen.findByRole("heading", {name: "Contacts"});
                expect(element).toBeInTheDocument();
            });

            it('renders first column correclty', async () => {
                setup();
                const column = await screen.findByTestId("contacts-column1");
                const entries = column.childNodes;
                expect(entries[0].textContent).toEqual("System register owner");
                expect(entries[1].textContent).toEqual("Business owner");
                expect(entries[2].textContent).toEqual("Technical owner");
                expect(entries[3].textContent).toEqual("Service owner");
                expect(entries[4].textContent).toEqual("Product owner");
                expect(entries[5].textContent).toEqual("Information asset owner");
            });

            it('renders correct entries in the second column of About table', async () => {
                setup();
                const column = await screen.findByTestId("contacts-column2");
                const entries = column.childNodes;
                expect(entries[0].textContent).toEqual("I own the register");
                expect(entries[1].textContent).toEqual("I own the business");
                expect(entries[2].textContent).toEqual("I own the tech");
                expect(entries[3].textContent).toEqual("I own the service");
                expect(entries[4].textContent).toEqual("I own the product");
                expect(entries[5].textContent).toEqual("I own the data");
            });
        });

        describe('Risk section', () => {
            it("renders Risk section", async () => {
                setup();
                const element = await screen.findByRole("heading", {name: "Risk"});
                expect(element).toBeInTheDocument();
            });

            it("displays correct risk names", async () => {
                setup();
                const roadmap = await screen.findByText("Roadmap");
                const techStack = await screen.findByText("Tech Stack");

                expect(roadmap).toBeInTheDocument();
                expect(techStack).toBeInTheDocument();
            });

            it("renders the details of each risk", async () => {
                setup();
                const risks = await screen.findAllByTestId('risk-details');
                expect(risks).toHaveLength(2);
            });
        });

    });

    describe('when the data is unknown', () => {
        beforeEach(() => {
            api.getSystem.mockResolvedValue(test_unknown_system);
        });

        describe('System section', () => {
            it('renders system name', async () => {
                setup();
                const element = await screen.findByText('System Y')
                expect(element).toBeInTheDocument()
            });

            it('displays correct modified on', async () => {
                setup();
                const element = await screen.findByTestId("system-last-modified")
                expect(element).toHaveTextContent("Never")
            });

            it('renders Description section', async () => {
                setup();
                const element = await screen.findByRole("heading", {name: "Description"});
                expect(element).toBeInTheDocument();
            });
        });

        describe('About section', () => {
            it('renders About section ', async () => {
                setup();
                const element = await screen.findByRole("heading", {name: "About"});
                expect(element).toBeInTheDocument();
            });

            it('renders first column correclty', async () => {
                setup();
                const column = await screen.findByTestId("about-column1");
                const entries = column.childNodes;
                expect(entries[0].textContent).toEqual("Aliases");
                expect(entries[1].textContent).toEqual("Department");
                expect(entries[2].textContent).toEqual("Criticality assessment");
                expect(entries[3].textContent).toEqual("Investment state");
                expect(entries[4].textContent).toEqual("Developed by");
                expect(entries[5].textContent).toEqual("Supported by");
            });

            it('renders correct entries in the second column of About table', async () => {
                setup();
                const column = await screen.findByTestId("about-column2");
                const entries = column.childNodes;
                expect(entries[0].textContent).toEqual("None");
                expect(entries[1].textContent).toEqual("UNKNOWN");
                expect(entries[2].textContent).toEqual("UNKNOWN");
                expect(entries[3].textContent).toEqual("INVESTMENT UNKNOWN");
                expect(entries[4].textContent).toEqual("UNKNOWN");
                expect(entries[5].textContent).toEqual("UNKNOWN");
            })

        });

        describe('Contacts section', () => {
            it('renders Contacts section', async () => {
                setup();
                const element = await screen.findByRole("heading", {name: "Contacts"});
                expect(element).toBeInTheDocument();
            });

            it('renders first column correclty', async () => {
                setup();
                const column = await screen.findByTestId("contacts-column1");
                const entries = column.childNodes;
                expect(entries[0].textContent).toEqual("System register owner");
                expect(entries[1].textContent).toEqual("Business owner");
                expect(entries[2].textContent).toEqual("Technical owner");
                expect(entries[3].textContent).toEqual("Service owner");
                expect(entries[4].textContent).toEqual("Product owner");
                expect(entries[5].textContent).toEqual("Information asset owner");
            });

            it('renders correct entries in the second column of About table', async () => {
                setup();
                const column = await screen.findByTestId("contacts-column2");
                const entries = column.childNodes;
                expect(entries[0].textContent).toEqual("UNKNOWN");
                expect(entries[1].textContent).toEqual("UNKNOWN");
                expect(entries[2].textContent).toEqual("UNKNOWN");
                expect(entries[3].textContent).toEqual("UNKNOWN");
                expect(entries[4].textContent).toEqual("UNKNOWN");
                expect(entries[5].textContent).toEqual("UNKNOWN");
            });
        })

        describe('Risk section', () => {
            it("renders Risk section", async () => {
                setup();
                const element = await screen.findByRole("heading", {name: "Risk"});
                expect(element).toBeInTheDocument();
            });

            it("displays correct risk names", async () => {
                setup();
                const roadmap = await screen.findByText("Roadmap");
                const techStack = await screen.findByText("Change");

                expect(roadmap).toBeInTheDocument();
                expect(techStack).toBeInTheDocument();
            });

            it("renders the details of each risk", async () => {
                setup();
                const risks = await screen.findAllByTestId('risk-details');
                expect(risks).toHaveLength(2);
            });
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
