import React from "react";
import { render as _render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

import RiskDetails from "../RiskDetails";

const test_risk_known = {
  name: "tech_stack",
  level: "low",
  rationale: "Fairly modern tech stack",
};

const test_risk_unknown = {
  name: "change",
  level: null,
  rationale: "Irrational",
};

const test_risk_not_applicable = {
  name: "sunset",
  level: "not_applicable",
  rationale: null,
};

const test_rationale_unknown = {
  name: "apple pie",
  level: "medium",
  rationale: null,
};

describe("<RiskDetails />", () => {
  it("renders risk title correctly", () => {
    const { getByText } = render(<RiskDetails risk={test_risk_known} />);
    const element = getByText("Tech Stack");
    expect(element).toBeInTheDocument();
  });

  it("renders level when value is known", () => {
    const { getByText } = render(<RiskDetails risk={test_risk_known} />);
    const element = getByText("LOW");
    expect(element).toBeInTheDocument();
  });

  it("renders level when value is not known", () => {
    const { getByText } = render(<RiskDetails risk={test_risk_unknown} />);
    const element = getByText("UNKNOWN");
    expect(element).toBeInTheDocument();
  });

  it("renders level when value is not applicable", () => {
    const { getByText } = render(
      <RiskDetails risk={test_risk_not_applicable} />
    );
    const element = getByText("N/A");
    expect(element).toBeInTheDocument();
  });

  it("renders rationale when value is known", () => {
    const { getByText } = render(<RiskDetails risk={test_risk_known} />);
    const element = getByText(/Rationale:/);
    expect(element).toHaveTextContent("Fairly modern tech stack");
  });

  it("renders rationale when value is not known", () => {
    const { getByText } = render(<RiskDetails risk={test_rationale_unknown} />);
    const element = getByText(/Rationale:/);
    expect(element).toContainHTML(
      `Rationale: <strong class="unknownKeyInfo">UNKNOWN</strong>`
    );
  });

  it("renders rationale when value is not applicable", () => {
    const { queryByText } = render(
      <RiskDetails risk={test_risk_not_applicable} />
    );
    const element = queryByText(/Rationale:/);
    expect(element).not.toBeInTheDocument();
  });

  it("links to the update risk page", () => {
    render(<RiskDetails risk={test_risk_known} />);

    const link = screen.getByRole("link");
    expect(link).toHaveAttribute(
      "href",
      expect.stringMatching(/\/update-risk\?lens=tech_stack$/)
    );
    expect(link).toHaveAttribute("title", "Update tech stack risk information");
  });

  function render(component) {
    return _render(<MemoryRouter>{component}</MemoryRouter>);
  }
});
