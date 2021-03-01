import React from "react";
import { render as _render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

import PageNotFoundError from "../PageNotFoundError";

function render(component) {
  return _render(component, { wrapper: MemoryRouter });
}

describe("PageNotFoundError", () => {
  it("renders", () => {
    render(<PageNotFoundError />);

    expect(screen.getByText("Page not found")).toBeInTheDocument();
  });

  it("has a title", () => {
    render(<PageNotFoundError />);

    expect(document.title).toBe("Page not found — System Register");
  });
});
