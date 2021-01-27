import { screen } from "@testing-library/dom";
import { render } from '@testing-library/react'
import React from "react";
import { Router } from 'react-router-dom'
import { createMemoryHistory } from 'history'

import SystemView from "../SystemView";

const test_system = {
  id: 444,
  name: "System X",
  aliases: ["sys X", "project Jupiter", "avalanche"],
  department: "Department of Fig and Indie",
  criticality: "high",
  investment_state: "evergreen",
  description: "The description",
  portfolio: 'Portfolio Y',
  last_updated: {
    timestamp: '2020-02-13T11:01:01',
    author_name: "Betty Franklin",
  },
  system_register_owner: "I own the register",
  business_owner: "I own the business",
  service_owner: "I own the service",
  tech_owner: "I own the tech",
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
  last_updated: {
    timestamp: "",
    author_name: "",
  },
  business_owner: "",
  service_owner: "",
  tech_owner: "",
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

describe("<SystemView>", () => {
  describe("When we have data", () => {
    describe("Renders system section", () => {
      it('renders system name', async () => {
        setup(test_system);
        const element = await screen.findByText('System X');
        expect(element).toBeInTheDocument();
      });

      it('renders a comma separated list of aliases sorted into alphabetical order if aliases are supplied', async () => {
        setup(test_system);
        const element = await screen.findByText('avalanche, project Jupiter, sys X')
        expect(element).toBeInTheDocument();
      });

      it('displays a message stating the system is not known by another name if no aliases are supplied', async () => {
        setup(test_unknown_system);
        const element = await screen.findByText('This system is not known by another name.')
        expect(element).toBeInTheDocument();
      });

      it('displays correct modified on', async () => {
        setup(test_system);
        const element = await screen.findByText('Last modified: 13 February 2020 by Betty Franklin');
        expect(element).toBeInTheDocument();
      });

      it('renders Description section', async () => {
        setup(test_system);
        const element = await screen.findByRole("heading", { name: "Description" });
        expect(element).toBeInTheDocument();
      });

      it('renders Change link for info section', async () => {
        setup(test_system);
        const element = await screen.getByTestId("info-change-link");
        expect(element).toBeInTheDocument();
        expect(element).toHaveAttribute("href", '//update-info') //TODO discuss with team if better way to do relative path with react-router-dom
      });


    })

    describe('About section', () => {
      it('renders About section', async () => {
        setup(test_system);
        const element = await screen.findByRole("heading", { name: "About" });
        expect(element).toBeInTheDocument();
      });

      it('renders Change link for about section', async () => {
        setup(test_system);
        const element = await screen.getByTestId("about-change-link");
        expect(element).toBeInTheDocument();
        expect(element).toHaveAttribute("href", '//update-about') //TODO discuss with team if better way to do relative path with react-router-dom
      });


      it('renders Change link for contacts section', async () => {
        setup(test_system);
        const element = await screen.getByTestId("contacts-change-link");
        expect(element).toBeInTheDocument();
        expect(element).toHaveAttribute("href", '//update-contacts') //TODO discuss with team if better way to do relative path with react-router-dom
      });

      it('renders first column correctly', async () => {
        setup(test_system);
        const column = await screen.findByTestId("about-column1");
        const entries = column.childNodes;
        expect(entries[0].textContent).toEqual("Portfolio");
        expect(entries[1].textContent).toEqual("Criticality assessment");
        expect(entries[2].textContent).toEqual("Investment state");
        expect(entries[3].textContent).toEqual("Developed by");
        expect(entries[4].textContent).toEqual("Supported by");
      });

      it('renders correct entries in the second column of About table', async () => {
        setup(test_system);
        const column = await screen.findByTestId("about-column2");
        const entries = column.childNodes;
        expect(entries[0].textContent).toEqual("Portfolio Y");
        expect(entries[1].textContent).toEqual("HIGH");
        expect(entries[2].textContent).toEqual("EVERGREEN");
        expect(entries[3].textContent).toEqual("Dev Team");
        expect(entries[4].textContent).toEqual("Support Team");
      })
    });

    describe('Contacts section', () => {
      it('renders Contacts section', async () => {
        setup(test_system);
        const element = await screen.findByRole("heading", { name: "Contacts" });
        expect(element).toBeInTheDocument();
      });

      it('renders first column correclty', async () => {
        setup(test_system);
        const column = await screen.findByTestId("contacts-column1");
        const entries = column.childNodes;
        expect(entries[0].textContent).toEqual("Business owner");
        expect(entries[1].textContent).toEqual("Technical owner");
        expect(entries[2].textContent).toEqual("Service owner");
        expect(entries[3].textContent).toEqual("Product owner");
        expect(entries[4].textContent).toEqual("Information asset owner");
      });

      it('renders correct entries in the second column of About table', async () => {
        setup(test_system);
        const column = await screen.findByTestId("contacts-column2");
        const entries = column.childNodes;
        expect(entries[0].textContent).toEqual("I own the business");
        expect(entries[1].textContent).toEqual("I own the tech");
        expect(entries[2].textContent).toEqual("I own the service");
        expect(entries[3].textContent).toEqual("I own the product");
        expect(entries[4].textContent).toEqual("I own the data");
      });
    });

    describe('Risk section', () => {
      it("renders Risk section", async () => {
        setup(test_system);
        const element = await screen.findByRole("heading", { name: "Risk" });
        expect(element).toBeInTheDocument();
      });

      it("displays correct risk names", async () => {
        setup(test_system);
        const roadmap = await screen.findByText("Roadmap");
        const techStack = await screen.findByText("Tech Stack");

        expect(roadmap).toBeInTheDocument();
        expect(techStack).toBeInTheDocument();
      });

      it("renders the details of each risk", async () => {
        setup(test_system);
        const risks = await screen.findAllByTestId('risk-details');
        expect(risks).toHaveLength(2);
      });
    });
  })

  describe("When we don't have data", () => {
    describe('System section', () => {
      it('renders system name', async () => {
        setup(test_unknown_system);
        const element = await screen.findByText('System Y')
        expect(element).toBeInTheDocument()
      });

      it('displays correct modified on', async () => {
        setup(test_unknown_system);
        const element = await screen.findByTestId("system-last-modified")
        expect(element).toHaveTextContent("Never")
      });

      it('renders Description section', async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", { name: "Description" });
        expect(element).toBeInTheDocument();
      });
    });

    describe('About section', () => {
      it('renders About section ', async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", { name: "About" });
        expect(element).toBeInTheDocument();
      });

      it('renders first column correctly', async () => {
        setup(test_unknown_system);
        const column = await screen.findByTestId("about-column1");
        const entries = column.childNodes;
        expect(entries[0].textContent).toEqual("Portfolio");
        expect(entries[1].textContent).toEqual("Criticality assessment");
        expect(entries[2].textContent).toEqual("Investment state");
        expect(entries[3].textContent).toEqual("Developed by");
        expect(entries[4].textContent).toEqual("Supported by");
      });

      it('renders correct entries in the second column of About table', async () => {
        setup(test_unknown_system);
        const column = await screen.findByTestId("about-column2");
        const entries = column.childNodes;
        expect(entries[0].textContent).toEqual("UNKNOWN");
        expect(entries[1].textContent).toEqual("UNKNOWN");
        expect(entries[2].textContent).toEqual("UNKNOWN");
        expect(entries[3].textContent).toEqual("UNKNOWN");
        expect(entries[4].textContent).toEqual("UNKNOWN");
      })

    });

    describe('Contacts section', () => {
      it('renders Contacts section', async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", { name: "Contacts" });
        expect(element).toBeInTheDocument();
      });

      it('renders first column correclty', async () => {
        setup(test_unknown_system);
        const column = await screen.findByTestId("contacts-column1");
        const entries = column.childNodes;
        expect(entries[0].textContent).toEqual("Business owner");
        expect(entries[1].textContent).toEqual("Technical owner");
        expect(entries[2].textContent).toEqual("Service owner");
        expect(entries[3].textContent).toEqual("Product owner");
        expect(entries[4].textContent).toEqual("Information asset owner");
      });

      it('renders correct entries in the second column of About table', async () => {
        setup(test_unknown_system);
        const column = await screen.findByTestId("contacts-column2");
        const entries = column.childNodes;
        expect(entries[0].textContent).toEqual("UNKNOWN");
        expect(entries[1].textContent).toEqual("UNKNOWN");
        expect(entries[2].textContent).toEqual("UNKNOWN");
        expect(entries[3].textContent).toEqual("UNKNOWN");
        expect(entries[4].textContent).toEqual("UNKNOWN");
      });
    });

    describe('Risk section', () => {
      it("renders Risk section", async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", { name: "Risk" });
        expect(element).toBeInTheDocument();
      });

      it("displays correct risk names", async () => {
        setup(test_unknown_system);
        const roadmap = await screen.findByText("Roadmap");
        const techStack = await screen.findByRole("heading", { name: "Change" });

        expect(roadmap).toBeInTheDocument();
        expect(techStack).toBeInTheDocument();
      });

      it("renders the details of each risk", async () => {
        setup(test_unknown_system);
        const risks = await screen.findAllByTestId('risk-details');
        expect(risks).toHaveLength(2);
      });
    })
  })
});

function setup(system) {
  const history = createMemoryHistory();
  render(<Router history={history}><SystemView system={system} /></Router>)
}