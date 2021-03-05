import React from "react";
import { render, screen } from "@testing-library/react";
import { createMemoryHistory } from "history";
import { Route, Router } from "react-router-dom";

import UpdateRisk from ".";

describe("UpdateRisk", () => {
  const returnPath = "/system/123";

  describe("system details have not been loaded", () => {
    it("informs the user that data is being loaded", () => {
      setUp(null);

      expect(screen.getByText("Loading system data...")).toBeVisible();
      expect(document.title).toBe("Loading system... — System Register");
    });
  });

  describe("system details are available", () => {
    it("displays the system name", () => {
      setUp({ name: "the system name", risks: [{ name: "risk lens" }] });

      expect(
        screen.getByRole("heading", { name: "the system name" })
      ).toBeVisible();
    });

    it("includes the risk in the page title", () => {
      setUp({ name: "the system name", risks: [{ name: "a lens" }] }, "a lens");

      expect(document.title).toBe(
        "Update a lens risk information — the system name — System Register"
      );
    });
  });

  describe("lens is invalid", () => {
    it("redirects the user to the system view when the lens is missing", () => {
      const { history } = setUp(
        { name: "name", risks: [{ name: "risk lens" }] },
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
        { name: "name", risks: [{ name: "a lens" }] },
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
          <UpdateRisk system={system} returnPath={returnPath} />
        </Route>
      </Router>
    );
    return { ...renderResult, history };
  }
});
