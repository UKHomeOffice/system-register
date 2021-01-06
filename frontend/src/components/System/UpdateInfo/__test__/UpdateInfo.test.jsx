import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import UpdateInfo from "../UpdateInfo";
import ValidationError from "../../../../services/validationError";

describe("UpdateInfo", () => {
  const submitHandler = jest.fn();

  beforeEach(() => {
    jest.resetAllMocks();

    submitHandler.mockResolvedValue(null);
  });

  it("displays the name of the system", () => {
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} />);

    expect(screen.getByRole("heading")).toHaveTextContent("system name");
  });

  it("displays a loading message if data is unavailable", () => {
    render(<UpdateInfo system={null} onSubmit={submitHandler} />);

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
  });

  it("calls submission handler with updated name", async () => {
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} executeCheck={() => false} />);
    const systemNameField = screen.getByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(systemNameField, "new system name");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      name: "new system name",
    }));
  });

  it("trims values before calling the submission handler", async () => {
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} executeCheck={() => false} />);
    const systemNameField = screen.getByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(systemNameField, "    new system name with spaces ");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      name: "new system name with spaces",
    }));
  });

  it.each(["system name", null])
  ("does not send unchanged values to the submission handler", async (value) => {
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} executeCheck={() => false} />);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({}));
  });

  it("calls cancel handler", () => {
    const cancelHandler = jest.fn();
    render(<UpdateInfo system={{ name: null }} onCancel={cancelHandler} executeCheck={() => false} />);
    const cancelButton = screen.getByRole("button", { name: /cancel/i });

    user.click(cancelButton);

    expect(cancelHandler).toBeCalled();
  });

  it("validates name before submission", async () => {
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} executeCheck={() => false} />);

    const productOwnerField = screen.getByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(productOwnerField, "$");
    user.click(saveButton);

    expect(await screen.findByText(/must not use the following special characters/i, { selector: "label *" })).toBeInTheDocument();
  });

  it("shows an error summary containing all error details", async () => {
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} executeCheck={() => false} />);
    const systemNameField = screen.getByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(systemNameField, "$");
    user.click(saveButton);

    expect(await screen.findByText(/must not use the following special characters/i, { selector: "a" })).toBeInTheDocument();
  });

  it("shows validation errors returned from the API", async () => {
    submitHandler.mockRejectedValue(new ValidationError({
      name: "validation error",
    }));
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} executeCheck={() => false} />);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(saveButton);

    expect(await screen.findByText(/validation error/i, { selector: "a" })).toBeInTheDocument();
  });
});
