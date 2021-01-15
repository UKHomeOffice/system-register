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

  describe("portfolio", () => {
    it("displays current portfolio button as checked", () => {
      render(<UpdateAbout system={{portfolio: "Option 1"}} onSubmit={submitHandler}/>);

      expect(screen.getByRole("radio", {name: /Option 1/i}).checked).toEqual(true);
    });

    it("calls submission handler with updated portfolio", async () => {
      render(<UpdateAbout system={{portfolio: "Option 1"}} onSubmit={submitHandler}/>);
      const unknownRadio = screen.getByLabelText(/Option 2/i);
      const saveButton = screen.getByRole("button", {name: /save/i});

      user.click(unknownRadio);
      user.click(saveButton);

      await waitFor(() => expect(submitHandler).toBeCalledWith({
        portfolio: "Option 2",
      }));
    });
  });

  describe("criticality", () =>
  {
    it("displays current criticality button as checked", () => {
      render(<UpdateAbout system={{criticality: "medium"}} onSubmit={submitHandler}/>);

      expect(screen.getByRole("radio", {name: /medium/i}).checked).toEqual(true);
    });

    it("calls submission handler with updated criticality", async () => {
      render(<UpdateAbout system={{criticality: "low"}} onSubmit={submitHandler}/>);
      const cniRadio = screen.getByLabelText(/cni/i);
      const saveButton = screen.getByRole("button", {name: /save/i});

      user.click(cniRadio);
      user.click(saveButton);

      await waitFor(() => expect(submitHandler).toBeCalledWith({
        criticality: "cni",
      }));
    });
  });

  describe("investment state", () => {
    it("displays current investment state button as checked", () => {
      render(<UpdateAbout system={{investment_state: "invest"}} onSubmit={submitHandler}/>);

      expect(screen.getByDisplayValue("invest").checked).toEqual(true);
    });

    it("calls submission handler with updated investment state", async () => {
      render(<UpdateAbout system={{investment_state: "invest"}} onSubmit={submitHandler}/>);
      const maintainRadio = screen.getByLabelText(/maintain/i);
      const saveButton = screen.getByRole("button", {name: /save/i});

      user.click(maintainRadio);
      user.click(saveButton);

      await waitFor(() => expect(submitHandler).toBeCalledWith({
        investmentState: "maintain",
      }));
    });
  });

  it("does NOT call submission handler when all field values are unchanged", async () => {
    const cancelHandler = jest.fn();
    render(<UpdateAbout system={{criticality: "low", investment_state: "invest", portfolio: "Option 1"}} onSubmit={submitHandler}
                        onCancel={cancelHandler}/>);
    const portfolioRadio = screen.getByLabelText(/Option 1/i);
    const criticalityRadio = screen.getByLabelText(/low/i);
    const investRadio = screen.getByDisplayValue(/invest/i);
    const saveButton = screen.getByRole("button", {name: /save/i});

    user.click(portfolioRadio);
    user.click(criticalityRadio);
    user.click(investRadio)
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({}));
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
