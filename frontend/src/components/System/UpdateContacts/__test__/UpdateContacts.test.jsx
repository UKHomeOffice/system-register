import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import UpdateContacts from "../UpdateContacts";
import ValidationError from "../../../../services/validationError";

describe("UpdateContacts", () => {
  const submitHandler = jest.fn();
  const cancelHandler = jest.fn();

  function setUp(system = {}) {
    const defaults = {
      name: "name",
    };
    return render(<UpdateContacts
      system={system ? { ...defaults, ...system } : null}
      onSubmit={submitHandler}
      onCancel={cancelHandler}
    />);
  }

  beforeEach(() => {
    jest.resetAllMocks();

    submitHandler.mockResolvedValue(null);
  });

  it("displays the name of the system", () => {
    setUp({ name: "system name" });

    expect(screen.getByRole("heading")).toHaveTextContent("system name");
  });

  it("displays a loading message if data is unavailable", () => {
    setUp(null);

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
  });

  it("calls submission handler with updated contacts", async () => {
    setUp({
      product_owner: "existing product owner",
      tech_owner: "existing tech owner",
      information_asset_owner: "existing information asset owner",
    });
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const technicalOwnerField = screen.getByLabelText(/technical owner/i);
    const informationAssetOwnerField = screen.getByLabelText(/information asset owner/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.clear(productOwnerField);
    user.clear(technicalOwnerField);
    user.clear(informationAssetOwnerField);
    // noinspection ES6MissingAwait
    user.type(productOwnerField, "new product owner");
    // noinspection ES6MissingAwait
    user.type(technicalOwnerField, "new technical owner");
    // noinspection ES6MissingAwait
    user.type(informationAssetOwnerField, "new information asset owner");

    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      productOwner: "new product owner",
      technicalOwner: "new technical owner",
      informationAssetOwner: "new information asset owner",
    }));
  });

  it("trims values before calling the submission handler", async () => {
    setUp();
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const technicalOwnerField = screen.getByLabelText(/technical owner/i);
    const informationAssetOwnerField = screen.getByLabelText(/information asset owner/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(productOwnerField, "  owner with extra spaces  ");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(technicalOwnerField, "  another owner with more spaces  ");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(informationAssetOwnerField, "  yet another owner with more spaces  ");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      productOwner: "owner with extra spaces",
      technicalOwner: "another owner with more spaces",
      informationAssetOwner: "yet another owner with more spaces",
    }));
  });

  it.each(["owner", null])
    ("does not send unchanged values to the submission handler", async (value) => {
      setUp({ product_owner: value, tech_owner: value });
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.click(saveButton);

      await waitFor(() => expect(submitHandler).toBeCalledWith({}));
    });

  it("calls cancel handler", () => {
    setUp();
    const cancelButton = screen.getByRole("button", { name: /cancel/i });

    user.click(cancelButton);

    expect(cancelHandler).toBeCalled();
  });

  it.each(["product owner", "technical owner", "information asset owner"])
    ("validates %p contact before submission", async (label) => {
      setUp();
      const ownerField = screen.getByLabelText(new RegExp(label, "i"));
      const saveButton = screen.getByRole("button", { name: /save/i });

      // noinspection ES6MissingAwait: there is no typing delay
      user.type(ownerField, "$");
      user.click(saveButton);

      expect(
        await screen.findByText(/must not use the following special characters/i, { selector: "label *" })
      ).toBeVisible();
    });

  it("shows an error summary containing all error details", async () => {
    setUp();
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const technicalOwnerField = screen.getByLabelText(/technical owner/i);
    const informationAssetOwnerField = screen.getByLabelText(/information asset owner/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(technicalOwnerField, "x");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(productOwnerField, "!?$");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(informationAssetOwnerField, "x");
    user.click(saveButton);

    const errors = await screen.findAllByText(/must/i, { selector: "a" });
    expect(errors).toHaveLength(3);
    expect(errors[0]).toHaveTextContent("incomplete");
    expect(errors[1]).toHaveTextContent("special characters");
    expect(errors[2]).toHaveTextContent("incomplete");
  });

  it("shows validation errors returned from the API", async () => {
    submitHandler.mockRejectedValue(new ValidationError({
      productOwner: "product validation error",
      technicalOwner: "tech validation error",
      informationAssetOwner: "information validation error",
    }));
    setUp();
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(saveButton);

    expect(await screen.findAllByText(/validation error/i, { selector: "a" })).toHaveLength(3);
  });
});
