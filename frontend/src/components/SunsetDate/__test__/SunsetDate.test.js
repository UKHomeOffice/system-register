import SunsetDate from "../SunsetDate";
import React from "react";
import { render, screen } from "@testing-library/react";

describe("<SunsetDate />", () => {
  describe("sunset date supplied", () => {
    it("renders correctly formatted date if date supplied", () => {
      render(<SunsetDate investment_state="unknown" date="2021-09-01" />);
      const sunsetDate = screen.getByText("1 September 2021");

      expect(sunsetDate).toBeInTheDocument();
    });
  });

  describe("no sunset date available", () => {
    it("displays unknown if investment state is unknown", () => {
      render(<SunsetDate investment_state="unknown" date={null} />);
      const badge = screen.getByText("UNKNOWN");

      expect(badge).toBeInTheDocument();
    });

    it.each(["evergreen", "invest", "maintain"])(
      "displays none if investment state is positive",
      (investmentState) => {
        render(<SunsetDate investment_state={investmentState} date={null} />);
        const badge = screen.getByText("NONE");

        expect(badge).toBeInTheDocument();
      }
    );

    it.each(["sunset", "decommissioned"])(
      "displays unknown if investment state is end of life",
      (investmentState) => {
        render(<SunsetDate investment_state={investmentState} date={null} />);
        const badge = screen.getByText("UNKNOWN");

        expect(badge).toBeInTheDocument();
      }
    );

    it("displays N/A if investment state is cancelled", () => {
      render(<SunsetDate investment_state="cancelled" date={null} />);
      const badge = screen.getByText("N/A");

      expect(badge).toBeInTheDocument();
    });
  });
});
