import React from "react";
import { render, screen } from "@testing-library/react";
import UpdateRiskForm from "./UpdateRiskForm";

describe("UpdateRiskForm", () => {
  const cancelHandler = jest.fn();

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("shows informational text that includes the risk lens", () => {
    setUp({ name: "lens_name", rationale: "" });

    expect(
      screen.getByText(/enter a rationale for lens name risks/)
    ).toBeVisible();
  });

  describe("risk rating", () => {
    it("shows the available ratings", () => {
      const options = [
        ["low", "Low"],
        ["medium", "Medium"],
        ["high", "High"],
        ["unknown", "Unknown"],
        ["not_applicable", "Not applicable"],
      ];

      setUp({ name: "lens_name", rationale: "" });

      const ratingOptions = screen.getAllByRole("radio");
      expect(ratingOptions).toHaveLength(5);
      ratingOptions.forEach((option, index) => {
        const [value, text] = options[index];
        expect(option.closest("label")).toHaveTextContent(
          new RegExp(`^${text}$`)
        );
        expect(option.value).toBe(value);
      });
    });

    it.each(["low", "medium", "high", "unknown", "not_applicable"])(
      "preselects the current rating: %p",
      (level) => {
        setUp({ name: "name", level, rationale: "" });

        expect(screen.getByDisplayValue(level)).toBeChecked();
      }
    );

    it.each([null, undefined])(
      "defaults missing values to unknown: %p",
      (level) => {
        setUp({ name: "name", level, rationale: "" });

        expect(screen.getByDisplayValue("unknown")).toBeChecked();
      }
    );
  });

  function setUp(risk, systemName = "system") {
    return render(
      <UpdateRiskForm
        risk={risk}
        systemName={systemName}
        onCancel={cancelHandler}
      />
    );
  }
});
