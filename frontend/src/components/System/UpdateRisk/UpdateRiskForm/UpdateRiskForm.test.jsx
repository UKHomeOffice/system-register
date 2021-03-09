import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import ValidationError from "../../../../services/validationError";

import UpdateRiskForm from ".";

describe("UpdateRiskForm", () => {
  const cancelHandler = jest.fn();
  const submitHandler = jest.fn();

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
    it("derives the heading from the lens", () => {
      setUp({ name: "a__lens_name", rationale: "" });

      expect(
        screen.getByRole("heading", { name: "A lens name risk rating" })
      ).toBeVisible();
    });

    it("shows the available ratings", () => {
      const options = [
        ["low", "Low", "Low risk rating"],
        ["medium", "Medium", "Medium risk rating"],
        ["high", "High", "High risk rating"],
        ["unknown", "Unknown", "Unknown risk rating"],
        ["not_applicable", "Not applicable", "Risk rating is not applicable"],
      ];

      setUp({ name: "lens_name", rationale: "" });

      const ratingOptions = screen.getAllByRole("radio");
      expect(ratingOptions).toHaveLength(5);
      ratingOptions.forEach((option, index) => {
        const [value, text, title] = options[index];
        expect(option.closest("label")).toHaveTextContent(
          new RegExp(`^${text}$`)
        );
        expect(option.value).toBe(value);
        expect(option).toHaveAttribute("title", title);
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

  describe("risk rationale", () => {
    it("derives the heading from the lens", () => {
      setUp({ name: "another_lens_name", rationale: "" });

      expect(
        screen.getByRole("textbox", { name: /^Another lens name rationale/ })
      ).toBeVisible();
    });

    it("pre-populates the existing rationale", () => {
      setUp({ name: "name", rationale: "a rationale" });

      expect(screen.getByRole("textbox", /rationale/i)).toHaveValue(
        "a rationale"
      );
    });

    it.each([null, undefined])(
      "defaults missing values to a blank rationale: %p",
      (rationale) => {
        setUp({ name: "name", rationale });

        expect(screen.getByRole("textbox", /rationale/i)).toHaveValue("");
      }
    );

    it("trims extraneous spaces during submission", async () => {
      setUp({ name: "name", rationale: "rationale" });
      const rationaleTextbox = screen.getByRole("textbox");
      const saveButton = screen.getByRole("button", { name: /save/i });

      overtype(rationaleTextbox, "\n  a new\f rationale \t with  spaces \v ");
      user.click(saveButton);

      await waitFor(() => {
        expect(submitHandler).toBeCalledWith(
          expect.objectContaining({
            rationale: "a new\f rationale \t with  spaces",
          })
        );
      });
    });

    it("is validated before submission", async () => {
      setUp({ name: "name", rationale: "rationale" });
      const rationaleTextbox = screen.getByRole("textbox");
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.clear(rationaleTextbox);
      user.click(saveButton);

      expect(
        await screen.findByText(/must enter a rationale/, {
          selector: "label *",
        })
      ).toBeVisible();
      expect(submitHandler).not.toBeCalled();
    });
  });

  describe("error messaging", () => {
    it("shows a summary containing all error details", async () => {
      setUp({ name: "name", rationale: "rationale" });
      const rationaleTextbox = screen.getByRole("textbox");
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.clear(rationaleTextbox);
      user.click(saveButton);

      await waitFor(() => {
        const error = screen.getByText(/must|enter/i, { selector: "a" });
        expect(error).toHaveTextContent("must enter a rationale");
      });
    });

    it("shows validation errors returned from the API", async () => {
      submitHandler.mockRejectedValue(
        new ValidationError({
          level: "level validation error",
          rationale: "rationale validation error",
        })
      );
      setUp({ name: "name", level: "low", rationale: "rationale" });
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.click(saveButton);

      expect(
        await screen.findAllByText(/validation error/i, { selector: "a" })
      ).toHaveLength(2);
    });

    it("indicates that there is problem in the title", async () => {
      setUp({ name: "name", rationale: "rationale" });
      const rationaleTextbox = screen.getByRole("textbox");
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.clear(rationaleTextbox);
      user.click(saveButton);

      await waitFor(() => {
        expect(document.title).toMatch(/^Error:/);
      });
    });
  });

  function setUp(risk, systemName = "system") {
    return render(
      <UpdateRiskForm
        risk={risk}
        systemName={systemName}
        onSubmit={submitHandler}
        onCancel={cancelHandler}
      />
    );
  }

  function overtype(element, text) {
    user.clear(element);
    user.type(element, text);
  }
});
