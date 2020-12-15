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

    user.type(productOwnerField, "new owner");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      productOwner: "new owner",
    }));
  });
});
