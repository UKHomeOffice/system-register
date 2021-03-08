import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";
import { createMemoryHistory } from "history";
import { Route, Router } from "react-router-dom";

import UpdateRisk from ".";

describe("UpdateRisk", () => {
  const cancelHandler = jest.fn();
  const submitHandler = jest.fn();
  const returnPath = "/system/123";

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

    it("sends changed values to the submit event callback", async () => {
      setUp(
        {
          name: "system",
          risks: [{ name: "lens", rationale: "existing rationale" }],
        },
        "lens"
      );
      const mediumRiskRating = screen.getByRole("radio", { name: /medium/i });
      const rationaleTextbox = screen.getByRole("textbox", {
        name: /rationale/i,
      });
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.click(mediumRiskRating);
      overtype(rationaleTextbox, "a new rationale");
      user.click(saveButton);

      await waitFor(() => {
        expect(submitHandler).toBeCalledWith({
          level: "medium",
          rationale: "a new rationale",
        });
      });
    });

    it.each([
      ["level", { level: "high", rationale: "existing rationale" }],
      ["rationale", { level: "low", rationale: "new rationale" }],
    ])(
      "excludes unchanged values from data sent to the submit event callback: %p",
      async (expectedFieldName, { level, rationale }) => {
        setUp(
          {
            name: "system",
            risks: [
              { name: "lens", level: "low", rationale: "existing rationale" },
            ],
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
            [expectedFieldName]: expect.any(String),
          });
        });
      }
    );

    it("triggers the cancel handler when the cancel button is clicked", () => {
      setUp(
        { name: "system", risks: [{ name: "lens", rationale: "" }] },
        "lens"
      );
      const cancelButton = screen.getByRole("button", { name: /cancel/i });

      user.click(cancelButton);

      expect(cancelHandler).toBeCalled();
    });
  });

  describe("lens is invalid", () => {
    it("redirects the user to the system view when the lens is missing", () => {
      const { history } = setUp(
        { name: "name", risks: [{ name: "risk lens", rationale: "" }] },
        null
      );

      expect(history.entries).toEqual([
        expect.objectContaining({
          pathname: returnPath,
        }),
      ]);
    });

    it("redirects the user to the system view when the lens does not apply to the system", () => {
      const { history } = setUp(
        { name: "name", risks: [{ name: "a lens", rationale: "" }] },
        "a different lens"
      );

      expect(history.entries).toEqual([
        expect.objectContaining({
          pathname: returnPath,
        }),
      ]);
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
            returnPath={returnPath}
            onSubmit={submitHandler}
            onCancel={cancelHandler}
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