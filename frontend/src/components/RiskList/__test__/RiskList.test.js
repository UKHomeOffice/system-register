import React from "react";
import { render, screen, within } from "@testing-library/react";
import RiskList from "../RiskList";
import { BrowserRouter } from "react-router-dom";

const test_data = [
  {
    id: 44,
    name: "Sys Name 1",
    aliases: ["Alias A"],
    risks: [
      { name: "roadmap", level: "low" },
      { name: "sunset", level: "not_applicable" },
    ],
  },
  {
    id: 1111,
    name: "Sys Name 2",
    portfolio: "No risk no reward",
    risks: [
      { name: "roadmap", level: "medium" },
      { name: "sunset", level: null },
    ],
  },
  {
    id: 33,
    name: "Sys Name 3",
    risks: [
      { name: "roadmap", level: "high" },
      { name: "sunset", level: "medium" },
    ],
  },
  {
    id: 21,
    name: "Sys Name 4",
    risks: [
      { name: "roadmap", level: "unknown" },
      { name: "sunset", level: undefined },
    ],
  },
];

describe("<RiskList />", () => {
  it("displays list of all systems when all risk lenses chosen", () => {
    const { getAllByText } = render(
      <BrowserRouter>
        <RiskList systems={test_data} riskLens="combined" />
      </BrowserRouter>
    );
    const systemElements = getAllByText(/Sys Name/);
    expect(systemElements.length).toEqual(4);
  });

  it("displays list of systems when sunset risk lenses chosen", () => {
    const { getAllByText } = render(
      <BrowserRouter>
        <RiskList systems={test_data} riskLens="sunset" />
      </BrowserRouter>
    );
    const systemElements = getAllByText(/Sys Name/);
    expect(systemElements.length).toEqual(4);
  });

  describe("when showing risk for a specific risk lens", () => {
    it("shows high risk when roadmap is chosen", () => {
      const { getByTestId } = renderRiskList();
      const element = getByTestId("risk_cell_Sys Name 3");
      expect(element).toHaveClass("highRiskBackground");
    });

    it("shows medium risk when roadmap is chosen", () => {
      const { getByTestId } = renderRiskList();
      const element = getByTestId("risk_cell_Sys Name 2");
      expect(element).toHaveClass("mediumRiskBackground");
    });

    it("shows low risk when roadmap is chosen", () => {
      const { getByTestId } = renderRiskList();
      const element = getByTestId("risk_cell_Sys Name 1");
      expect(element).toHaveClass("lowRiskBackground");
    });

    it("shows unknown risk when sunset is chosen", () => {
      const { getByTestId } = renderRiskList(
        test_data,
        "portfolio",
        "Portfolio A",
        "sunset"
      );
      const element = getByTestId("risk_cell_Sys Name 2");
      expect(element).toHaveClass("unknownRiskBackground");
    });

    it("shows risk as not applicable when lens is absent for a system", () => {
      renderRiskList(
        [
          ...test_data,
          {
            id: 678,
            name: "system without sunset lens",
            portfolio: "portfolio",
            criticality: "cni",
            risks: [{ name: "roadmap", level: "medium" }],
          },
        ],
        "portfolio",
        null,
        "sunset"
      );

      const systemRow = screen
        .getByRole("link", { name: "system without sunset lens" })
        .closest("tr");
      expect(within(systemRow).getByText("UNKNOWN")).toBeVisible();
    });
  });

  describe("when showing risk for all risk lenses", () => {
    it("shows shows System 3 as mediumHigh risk", () => {
      const { getByTestId } = renderRiskList(
        test_data,
        "str",
        "str",
        "combined"
      );
      const element = getByTestId("risk_cell_Sys Name 3");
      expect(element).toHaveClass("mediumHighRiskBackground");
    });

    it("shows shows System 2 as medium risk", () => {
      const { getByTestId } = renderRiskList(
        test_data,
        "str",
        "str",
        "combined"
      );
      const element = getByTestId("risk_cell_Sys Name 2");
      expect(element).toHaveClass("mediumLowRiskBackground");
    });

    it("shows shows System 1 as low risk", () => {
      const { getByTestId } = renderRiskList(
        test_data,
        "str",
        "str",
        "combined"
      );
      const element = getByTestId("risk_cell_Sys Name 1");
      expect(element).toHaveClass("lowRiskBackground");
    });

    it("shows shows System 4 as unknown risk", () => {
      const { getByTestId } = renderRiskList(
        test_data,
        "str",
        "str",
        "combined"
      );
      const element = getByTestId("risk_cell_Sys Name 1");
      expect(element).toHaveClass("lowRiskBackground");
    });
  });
  //todo test orders by high, med, low, n/a, unknown
});

function renderRiskList(
  systems = test_data,
  rootType = "portfolio",
  rootValue = "Portfolio A",
  riskLens = "roadmap"
) {
  return render(
    <BrowserRouter>
      <RiskList
        systems={systems}
        rootType={rootType}
        rootValue={rootValue}
        riskLens={riskLens}
      />
    </BrowserRouter>
  );
}
