import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import UpdateAbout from "../UpdateAbout";

describe("UpdateAbout", () => {
  const submitHandler = jest.fn();
  const cancelHandler = jest.fn();

  function setUp(props = {}) {
    const actualProps = {
      system: null,
      portfolios: [],
      onSubmit: submitHandler,
      onCancel: cancelHandler,
      withSupportedBy: true,
      ...props,
    };
    return render(<UpdateAbout {...actualProps} />);
  }

  function overtype(field, value) {
    user.clear(field);
    // noinspection JSIgnoredPromiseFromCall
    user.type(field, value);
  }

  beforeEach(() => {
    jest.resetAllMocks();

    submitHandler.mockResolvedValue(null);
  });

  it("displays the name of the system", () => {
    setUp({ system: {name: 'system name' }})

    expect(screen.getByRole("heading", { level: 1 })).toHaveTextContent("system name");
  });

  it("displays a loading message if data is unavailable", () => {
    setUp({system: null});

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
  });

  describe("portfolio", () => {
    it("displays current portfolio button as checked", () => {
      setUp({system: {portfolio: "Option 1"}, portfolios: ["Option 1"]});
      expect(screen.getByRole("radio", {name: /Option 1/i}).checked).toEqual(true);
    });

    it("calls submission handler with updated portfolio", async () => {
      setUp({system: {portfolio: "Option 1"}, portfolios: ["Option 1", "Option 2"]});
      const unknownRadio = screen.getByLabelText(/Option 2/i);
      const saveButton = screen.getByRole("button", {name: /save/i});

      user.click(unknownRadio);
      user.click(saveButton);

      await waitFor(() => expect(submitHandler).toBeCalledWith({
        portfolio: "Option 2",
      }));
    });
  });

  describe("criticality", () => {
    it("displays current criticality button as checked", () => {
      setUp({system: {criticality: "medium"}});

      expect(screen.getByRole("radio", {name: /medium/i}).checked).toEqual(true);
    });

    it("calls submission handler with updated criticality", async () => {
      setUp({system: {criticality: "low"}});

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
      setUp({system: {investment_state: "invest"}});

      expect(screen.getByDisplayValue("invest").checked).toEqual(true);
    });

    it("calls submission handler with updated investment state", async () => {
      setUp({system: {investment_state: "invest"}});
      const maintainRadio = screen.getByLabelText(/maintain/i);
      const saveButton = screen.getByRole("button", {name: /save/i});

      user.click(maintainRadio);
      user.click(saveButton);

      await waitFor(() => expect(submitHandler).toBeCalledWith({
        investmentState: "maintain",
      }));
    });
  });

  describe("supported by", () => {
    it("calls submission handler with updated supported by value", async () => {
      setUp({ system: { supported_by: "original supporter" } });
      const textField = screen.getByLabelText(/who supports/i);
      const saveButton = screen.getByRole("button", {name: /save/i});

      overtype(textField, "new supporter");
      user.click(saveButton);

      await waitFor(() => expect(submitHandler).toBeCalledWith({
        supportedBy: "new supporter",
      }));
    });

    it("validates before submission", async () => {
      setUp({ system: { supported_by: "someone" }});
      const textField = await screen.findByLabelText(/who supports/i);
      const saveButton = screen.getByRole("button", { name: /save/i });

      overtype(textField, "$");
      user.click(saveButton);

      expect(
        await screen.findByText(/must not use the following special characters/i, { selector: "label *" })
      ).toBeVisible();
    });
  });

  it("does NOT call submission handler when all field values are unchanged", async () => {
    setUp({
      system: { criticality: "low", investment_state: "invest", portfolio: "Option 1", supported_by: "person" },
      portfolios: ["Option 1"]
    });
    const portfolioRadio = screen.getByLabelText(/Option 1/i);
    const criticalityRadio = screen.getByLabelText(/low/i);
    const investRadio = screen.getByDisplayValue(/invest/i);
    const supportedByField = screen.getByLabelText(/who supports/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(portfolioRadio);
    user.click(criticalityRadio);
    user.click(investRadio);
    overtype(supportedByField, "person");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({}));
  });

  it("cancels correctly", async () => {
    setUp({system: {criticality: "low"}});
    const cniRadio = screen.getByLabelText(/cni/i);
    const cancelButton = screen.getByRole("button", { name: /cancel/i });

    user.click(cniRadio);
    user.click(cancelButton);

    await waitFor(() => expect(submitHandler).not.toHaveBeenCalled());
    await waitFor(() => expect(cancelHandler).toHaveBeenCalled());
  });
});
