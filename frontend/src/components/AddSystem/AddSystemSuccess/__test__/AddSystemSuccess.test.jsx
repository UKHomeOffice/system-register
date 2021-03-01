import React from "react";
import { MemoryRouter, Route, Router } from "react-router-dom";
import { render, screen, waitFor } from "@testing-library/react";
import { createMemoryHistory } from "history";

import AddSystemSuccess from "../AddSystemSuccess";

describe("AddSystemSuccess", () => {
  it("has a page title", () => {
    render(
      <MemoryRouter initialEntries={[{ state: {} }]}>
        <AddSystemSuccess returnPath="" />
      </MemoryRouter>
    );

    expect(document.title).toBe(
      "New system added successfully — System Register"
    );
  });

  it("replaces the success page with the return path when no location state is available", async () => {
    const history = createMemoryHistory({ initialEntries: ["/success"] });
    render(
      <Router history={history}>
        <Route path="/success">
          <AddSystemSuccess returnPath="/replacement-path" />
        </Route>
      </Router>
    );

    expect(
      screen.queryByRole("heading", { name: /system added/i })
    ).not.toBeInTheDocument();
    await waitFor(() => {
      expect(history.location.pathname).toEqual("/replacement-path");
    });
  });
});
