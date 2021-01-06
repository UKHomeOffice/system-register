import React from "react";
import { render, screen } from "@testing-library/react";

import Contact from ".";

describe("Contact", () => {
  it("has a heading", () => {
    render(<Contact />);

    expect(screen.getByRole("heading", { level: 1 })).toHaveTextContent("Get in touch");
  });
});
