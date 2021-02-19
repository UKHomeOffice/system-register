import React from "react";

import { render, screen } from "@testing-library/react";
import { createMemoryHistory } from "history";
import { Route, Router } from "react-router-dom";

import AddSystem from "../AddSystem";

jest.mock("../../../services/api", () => ({
  addSystem: jest.fn(),
}));

const addHandler = jest.fn();

describe("<AddSystem />", () => {
  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("renders add system form", async () => {
    const history = createMemoryHistory({ initialEntries: ["/add-system"] });
    render(
      <Router history={history}>
        <Route path="/add-system">
          <AddSystem validateNewSystem={() => undefined} onAdd={addHandler} />
        </Route>
      </Router>
    );

    const element = await screen.findByText("Add a system to the register");

    expect(element).toBeInTheDocument();
  });
});
