import React from "react";
import { render, screen } from "@testing-library/react";

import About from ".";

describe("About", () => {
  it("has a heading", () => {
    render(<About />);

    expect(screen.getByRole("heading", { level: 1 })).toHaveTextContent(
      "About the System Register"
    );
  });

  it("has a page title", () => {
    render(<About />);

    expect(document.title).toBe("About — System Register");
  });
});
