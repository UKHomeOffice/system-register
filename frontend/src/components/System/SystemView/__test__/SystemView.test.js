import { render, screen, within } from "@testing-library/react";

import React from "react";
import { Router } from "react-router-dom";
import { createMemoryHistory } from "history";

import SystemView from "../SystemView";

const test_system = {
  id: 444,
  name: "System X",
  aliases: ["sys X", "project Jupiter", "avalanche"],
  department: "Department of Fig and Indie",
  criticality: "high",
  investment_state: "evergreen",
  description: "The description",
  portfolio: "Portfolio Y",
  last_updated: {
    timestamp: "2020-02-13T11:01:01",
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
      name: "roadmap",
      level: "medium",
      rationale: "there is no plan",
    },
    {
      name: "tech_stack",
      level: "low",
      rationale: "Really good tech",
    },
  ],
  sunset: {
    date: "2021-06-01",
    additional_information: "some sunset info",
  },
};

const test_unknown_system = {
  id: 444,
  name: "System Y",
  aliases: [],
  criticality: "",
  investment_state: "",
  description: "",
  portfolio: "",
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
      name: "roadmap",
      level: null,
      rationale: null,
    },
    {
      name: "change",
      level: null,
      rationale: null,
    },
  ],
  sunset: {
    date: null,
    additional_information: null,
  },
};

describe("SystemView", () => {
  const closeHandler = jest.fn();

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it.each([
    [test_system, "System X — System Register"],
    [null, "Loading system... — System Register"],
  ])("has a page title", (system, title) => {
    setup(system);

    expect(document.title).toBe(title);
  });

  describe("When we have data", () => {
    describe("update notifications", () => {
      it("displays a success message after updating a system", () => {
        setup(test_system, { status: "success" });

        expect(document.title).toEqual(
          expect.stringMatching(/^Update saved\b/)
        );
        expect(screen.getByText(/update has been saved/i)).toBeInTheDocument();
      });
    });

    describe("Renders system section", () => {
      it("renders system name", async () => {
        setup(test_system);
        const element = await screen.findByText("System X");
        expect(element).toBeInTheDocument();
      });

      it("renders a comma separated list of aliases sorted into alphabetical order if aliases are supplied", async () => {
        setup(test_system);
        const element = await screen.findByText(
          "avalanche, project Jupiter, sys X"
        );
        expect(element).toBeInTheDocument();
      });

      it("displays a message stating the system is not known by another name if no aliases are supplied", async () => {
        setup(test_unknown_system);
        const element = await screen.findByText(
          "This system is not known by another name."
        );
        expect(element).toBeInTheDocument();
      });

      it("displays correct modified on", async () => {
        setup(test_system);
        const element = await screen.findByText(
          "Last modified: 13 February 2020 by Betty Franklin"
        );
        expect(element).toBeInTheDocument();
      });

      it("renders Description section", async () => {
        setup(test_system);
        const element = await screen.findByRole("heading", {
          name: "Description",
        });
        expect(element).toBeInTheDocument();
      });

      it("renders Change link for info section", () => {
        setup(test_system);
        const element = screen.getByTestId("info-change-link");
        expect(element).toBeInTheDocument();
        expect(element).toHaveAttribute("href", "//update-info");
      });
    });

    describe("About section", () => {
      it("renders About section", async () => {
        setup(test_system);
        const element = await screen.findByRole("heading", { name: "About" });
        expect(element).toBeInTheDocument();
      });

      it("renders Change link for about section", () => {
        setup(test_system);
        const element = screen.getByTestId("about-change-link");
        expect(element).toBeInTheDocument();
        expect(element).toHaveAttribute("href", "//update-about");
      });

      it.each([
        ["Portfolio", "Portfolio Y"],
        ["Criticality assessment", "HIGH"],
        ["Investment state", "EVERGREEN"],
        ["Developed by", "Dev Team"],
        ["Supported by", "Support Team"],
      ])("renders correct entries in the About table: %p", (label, value) => {
        setup(test_system);
        const row = screen.getByText(label).closest(".system-view-row");
        const cell = within(row).getByText(value);

        expect(cell).toBeInTheDocument();
      });
    });

    describe("Key dates section", () => {
      it("renders a Key dates section", async () => {
        setup(test_system);
        const element = await screen.findByRole("heading", {
          name: "Key dates",
        });

        expect(element).toBeInTheDocument();
      });

      it("renders Change link for key dates section", () => {
        setup(test_system);
        const element = screen.getByTestId("key-dates-change-link");
        expect(element).toBeInTheDocument();
        expect(element).toHaveAttribute("href", "//update-key-dates");
      });

      it("displays a sunset date", async () => {
        setup(test_system);
        const row = screen.getByText("Sunset date").closest(".system-view-row");
        const cell = within(row).getByText("1 June 2021");

        expect(cell).toBeInTheDocument();
      });

      it("displays additional sunset information", async () => {
        setup(test_system);
        const row = screen
          .getByText("Additional information")
          .closest(".system-view-row");
        const cell = within(row).getByText("some sunset info");

        expect(cell).toBeInTheDocument();
      });
    });

    describe("Contacts section", () => {
      it("renders Contacts section", async () => {
        setup(test_system);
        const element = await screen.findByRole("heading", {
          name: "Contacts",
        });
        expect(element).toBeInTheDocument();
      });

      it("renders Change link for contacts section", () => {
        setup(test_system);
        const element = screen.getByTestId("contacts-change-link");
        expect(element).toBeInTheDocument();
        expect(element).toHaveAttribute("href", "//update-contacts");
      });

      it.each([
        ["Business owner", "I own the business"],
        ["Technical owner", "I own the tech"],
        ["Service owner", "I own the service"],
        ["Product owner", "I own the product"],
        ["Information asset owner", "I own the data"],
      ])(
        "renders correct entries in the contacts table: %p",
        (label, value) => {
          setup(test_system);
          const row = screen.getByText(label).closest(".system-view-row");
          const cell = within(row).getByText(value);

          expect(cell).toBeInTheDocument();
        }
      );
    });

    describe("Risk section", () => {
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
        const risks = await screen.findAllByTestId("risk-details");
        expect(risks).toHaveLength(2);
      });
    });
  });

  describe("When we don't have data", () => {
    describe("System section", () => {
      it("renders system name", async () => {
        setup(test_unknown_system);
        const element = await screen.findByText("System Y");
        expect(element).toBeInTheDocument();
      });

      it("displays correct modified on", async () => {
        setup(test_unknown_system);
        const element = await screen.findByTestId("system-last-modified");
        expect(element).toHaveTextContent("Never");
      });

      it("renders Description section", async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", {
          name: "Description",
        });
        expect(element).toBeInTheDocument();
      });
    });

    describe("About section", () => {
      it("renders About section ", async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", { name: "About" });
        expect(element).toBeInTheDocument();
      });

      it.each([
        ["Portfolio"],
        ["Criticality assessment"],
        ["Investment state"],
        ["Developed by"],
        ["Supported by"],
      ])("renders unknown values in the About table: %p", (label) => {
        setup(test_unknown_system);
        const row = screen.getByText(label).closest(".system-view-row");
        const cell = within(row).getByText("UNKNOWN");

        expect(cell).toBeInTheDocument();
      });
    });

    describe("Key dates section", () => {
      it("renders a Key dates section", async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", {
          name: "Key dates",
        });

        expect(element).toBeInTheDocument();
      });
    });

    describe("Contacts section", () => {
      it("renders Contacts section", async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", {
          name: "Contacts",
        });
        expect(element).toBeInTheDocument();
      });

      it.each([
        ["Business owner"],
        ["Technical owner"],
        ["Service owner"],
        ["Product owner"],
        ["Information asset owner"],
      ])("renders unknown values in the Contacts table: %p", (label) => {
        setup(test_unknown_system);
        const row = screen.getByText(label).closest(".system-view-row");
        const cell = within(row).getByText("UNKNOWN");

        expect(cell).toBeInTheDocument();
      });
    });

    describe("Risk section", () => {
      it("renders Risk section", async () => {
        setup(test_unknown_system);
        const element = await screen.findByRole("heading", { name: "Risk" });
        expect(element).toBeInTheDocument();
      });

      it("displays correct risk names", async () => {
        setup(test_unknown_system);
        const roadmap = await screen.findByText("Roadmap");
        const techStack = await screen.findByRole("heading", {
          name: "Change",
        });

        expect(roadmap).toBeInTheDocument();
        expect(techStack).toBeInTheDocument();
      });

      it("renders the details of each risk", async () => {
        setup(test_unknown_system);
        const risks = await screen.findAllByTestId("risk-details");
        expect(risks).toHaveLength(2);
      });
    });
  });

  function setup(system, options = {}) {
    const history = createMemoryHistory();
    const { status } = options;
    render(
      <Router history={history}>
        <SystemView system={system} status={status} onClose={closeHandler} />
      </Router>
    );
  }
});
