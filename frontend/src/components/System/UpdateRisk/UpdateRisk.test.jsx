import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";
import { createMemoryHistory } from "history";
import { Route, Router } from "react-router-dom";

import UpdateRisk from ".";

describe("UpdateRisk", () => {
  const closeHandler = jest.fn();
  const submitHandler = jest.fn();

  beforeEach(() => {
    jest.resetAllMocks();
  });

  describe("system details have not been loaded", () => {
    it("informs the user that data is being loaded", () => {
      setUp(null);

      expect(screen.getByText("Loading system data...")).toBeVisible();
      expect(document.title).toBe("Loading system... — System Register");
    });
  });

  describe("system details are available", () => {
    it("displays the system name", () => {
      setUp({
        name: "the system name",
        risks: [{ name: "risk lens", rationale: "" }],
      });

      expect(
        screen.getByRole("heading", { name: "the system name" })
      ).toBeVisible();
    });

    it("includes the risk in the page title", () => {
      setUp(
        { name: "the system name", risks: [{ name: "a lens", rationale: "" }] },
        "a lens"
      );

      expect(document.title).toBe(
        "Update a lens risk information — the system name — System Register"
      );
    });

    it.each([
      ["all", "medium", "a new rationale"],
      ["only rating", "high", "existing rationale"],
      ["only rationale", "unknown", "a new rationale"],
    ])(
      "sends all values to the submit event callback if %s changed",
      async (field, level, rationale) => {
        setUp(
          {
            name: "system",
            risks: [{ name: "lens", rationale: "existing rationale" }],
          },
          "lens"
        );
        const riskRating = screen.getByRole("radio", {
          name: new RegExp(level, "i"),
        });
        const rationaleTextbox = screen.getByRole("textbox", {
          name: /rationale/i,
        });
        const saveButton = screen.getByRole("button", { name: /save/i });

        user.click(riskRating);
        overtype(rationaleTextbox, rationale);
        user.click(saveButton);

        await waitFor(() => {
          expect(submitHandler).toBeCalledWith({
            lens: "lens",
            level,
            rationale,
          });
        });
      }
    );

    it("sends no data to the submit event callback if nothing changed", async () => {
      setUp(
        {
          name: "system",
          risks: [
            { name: "lens", level: "low", rationale: "existing rationale" },
          ],
        },
        "lens"
      );
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.click(saveButton);

      await waitFor(() => {
        expect(submitHandler).toBeCalledWith({});
      });
    });

    it("triggers the cancel handler when the cancel button is clicked", () => {
      setUp(
        { name: "system", risks: [{ name: "lens", rationale: "" }] },
        "lens"
      );
      const cancelButton = screen.getByRole("button", { name: /cancel/i });

      user.click(cancelButton);

      expect(closeHandler).toBeCalled();
    });
  });

  describe("lens is invalid", () => {
    it("redirects the user to the system view when the lens is missing", () => {
      setUp(
        { name: "name", risks: [{ name: "risk lens", rationale: "" }] },
        null
      );

      expect(closeHandler).toBeCalled();
    });

    it("redirects the user to the system view when the lens does not apply to the system", () => {
      setUp(
        { name: "name", risks: [{ name: "a lens", rationale: "" }] },
        "a different lens"
      );

      expect(closeHandler).toBeCalled();
    });
  });

  function setUp(system, lens = "risk lens") {
    const queryParams = lens ? `?lens=${lens}` : "";
    const history = createMemoryHistory({
      initialEntries: [`/update-risk${queryParams}`],
    });
    const renderResult = render(
      <Router history={history}>
        <Route path="/update-risk">
          <UpdateRisk
            system={system}
            onSubmit={submitHandler}
            onClose={closeHandler}
          />
        </Route>
      </Router>
    );
    return { ...renderResult, history };
  }

  function overtype(element, text) {
    user.clear(element);
    user.type(element, text);
  }
});
