import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import AddSystem from "../AddSystem";

function overtype(field, value) {
  user.clear(field);
  // noinspection JSIgnoredPromiseFromCall
  user.type(field, value);
}

describe("add system", () => {
  const submitHandler = jest.fn();

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

  it("calls submission handler with new system name", async () => {
    render(<AddSystem onSubmit={submitHandler} />);
    const systemNameField = await screen.findByLabelText(/system name/i);
    const saveButton = await screen.findByText(/save/i);

    overtype(systemNameField, "new system name");
    user.click(saveButton);

    await waitFor(() =>
      expect(submitHandler).toBeCalledWith({
        name: "new system name",
      })
    );
  });
});
