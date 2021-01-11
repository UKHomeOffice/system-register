import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import UpdateAbout from "../UpdateAbout";

describe("UpdateAbout", () => {
  const submitHandler = jest.fn();

  beforeEach(() => {
    jest.resetAllMocks();

    submitHandler.mockResolvedValue(null);
  });

  it("displays the name of the system", () => {
    render(<UpdateAbout system={{ name: 'system name' }} onSubmit={submitHandler} />);

    expect(screen.getByRole("heading", { level: 1 })).toHaveTextContent("system name");
  });

  it("displays a loading message if data is unavailable", () => {
    render(<UpdateAbout system={null} onSubmit={submitHandler} />);

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
  });

  it("displays current criticality button as checked", () => {
    render(<UpdateAbout system={{ criticality: "medium" }} onSubmit={submitHandler} />);

    expect(screen.getByRole("radio", { name: /medium/i }).checked).toEqual(true);
  });

  it("calls submission handler with updated criticality", async () => {
    render(<UpdateAbout system={{ criticality: "low" }} onSubmit={submitHandler} />);
    const cniRadio = screen.getByLabelText(/cni/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(cniRadio);
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      criticality: "cni",
    }));
  });

  it("does NOT call submission handler when criticality and investment state both unchanged", async () => {
    const cancelHandler = jest.fn();
    render(<UpdateAbout system={{ criticality: "low", investment_state: "invest"}} onSubmit={submitHandler} onCancel={cancelHandler} />);
    const lowRadio = screen.getByLabelText(/low/i);
    const investRadio = screen.getByLabelText(/invest/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(investRadio)
    user.click(lowRadio);
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({}));
  });

  it("displays current investment state button as checked", () => {
    render(<UpdateAbout system={{ investment_state: "invest" }} onSubmit={submitHandler} />);

    expect(screen.getByRole("radio", { name: /invest/i }).checked).toEqual(true);
  });

  it("calls submission handler with updated investment state", async () => {
    render(<UpdateAbout system={{ investment_state: "invest" }} onSubmit={submitHandler} />);
    const maintainRadio = screen.getByLabelText(/maintain/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(maintainRadio);
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      investmentState: "maintain",
    }));
  });

  it("cancels correctly", async () => {
    const cancelHandler = jest.fn();
    render(<UpdateAbout system={{ criticality: "low" }} onCancel={cancelHandler} />);
    const cniRadio = screen.getByLabelText(/cni/i);
    const cancelButton = screen.getByRole("button", { name: /cancel/i });

    user.click(cniRadio);
    user.click(cancelButton);

    await waitFor(() => expect(submitHandler).not.toHaveBeenCalled());
    await waitFor(() => expect(cancelHandler).toHaveBeenCalled());
  });
});
