import React from "react";
import { render, screen } from "@testing-library/react";

import AddSystem from "../AddSystem";

describe("add system", () => {
  it("provides an initial empty system name field", async () => {
    render(<AddSystem />);

    const systemNameField = await screen.findByLabelText(/system name/i);

    expect(systemNameField).toBeInTheDocument();
    expect(systemNameField.value).toBe("");
  });

  it("displays a save button", async () => {
    render(<AddSystem />);

    const saveButton = await screen.findByText(/save/i);

    expect(saveButton).toBeInTheDocument();
  });
});
