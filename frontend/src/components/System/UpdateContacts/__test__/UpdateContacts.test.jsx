import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import UpdateContacts from "../UpdateContacts";

describe("UpdateContacts", () => {
  const submitHandler = jest.fn();

  beforeEach(() => {
    jest.resetAllMocks();

    submitHandler.mockResolvedValue(null);
  });

  it("displays the name of the system", () => {
    render(<UpdateContacts system={{ name: 'system name' }} onSubmit={submitHandler} />);

    expect(screen.getByRole("heading")).toHaveTextContent("system name");
  });

  it("displays a loading message if data is unavailable", () => {
    render(<UpdateContacts system={null} onSubmit={submitHandler} />);

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
  });

  it("calls submission handler with updated contacts", async () => {
    render(<UpdateContacts system={{ product_owner: "existing owner" }} onSubmit={submitHandler} />);
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(productOwnerField, "new owner");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      productOwner: "new owner",
    }));
  });

  it("trims values before calling the submission handler", async () => {
    render(<UpdateContacts system={{ product_owner: null }} onSubmit={submitHandler} />);
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(productOwnerField, "  owner with extra spaces  ");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      productOwner: "owner with extra spaces",
    }));
  });

  it.each(["owner", null])
  ("does not send unchanged values to the submission handler", async (value) => {
    render(<UpdateContacts system={{ product_owner: value }} onSubmit={submitHandler} />);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({}));
  });

  it("calls cancel handler", () => {
    const cancelHandler = jest.fn();
    render(<UpdateContacts system={{ product_owner: null }} onCancel={cancelHandler} />);
    const cancelButton = screen.getByRole("button", {name: /cancel/i});

    user.click(cancelButton);

    expect(cancelHandler).toBeCalled();
  });

  it("validates contacts before submission", async () => {
    render(<UpdateContacts system={{ product_owner: null }} onSubmit={submitHandler} />);
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(productOwnerField, "$");
    user.click(saveButton);

    expect(await screen.findByText(/must not use the following special characters/i)).toBeInTheDocument();
  });
});
