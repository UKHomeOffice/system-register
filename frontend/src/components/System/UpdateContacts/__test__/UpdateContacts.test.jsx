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
    return render(
      <UpdateContacts
        system={system ? { ...defaults, ...system } : null}
        onSubmit={submitHandler}
        onCancel={cancelHandler}
      />
    );
  }

  beforeEach(() => {
    jest.resetAllMocks();

    submitHandler.mockResolvedValue(null);
  });

  it("displays the name of the system", () => {
    setUp({ name: "system name" });

    expect(screen.getByRole("heading")).toHaveTextContent("system name");
  });

  it("has a page title", () => {
    setUp({ name: "System X" });

    expect(document.title).toBe("Update contacts — System X — System Register");
  });

  it("displays a loading message if data is unavailable", () => {
    setUp(null);

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
  });

  it("calls submission handler with updated contacts", async () => {
    setUp({
      business_owner: "existing business owner",
      product_owner: "existing product owner",
      tech_owner: "existing tech owner",
      service_owner: "existing service owner",
      information_asset_owner: "existing information asset owner",
    });
    const businessOwnerField = screen.getByLabelText(/business owner/i);
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const technicalOwnerField = screen.getByLabelText(/technical owner/i);
    const serviceOwnerField = screen.getByLabelText(/service owner/i);
    const informationAssetOwnerField = screen.getByLabelText(
      /information asset owner/i
    );
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.clear(businessOwnerField);
    user.clear(productOwnerField);
    user.clear(technicalOwnerField);
    user.clear(serviceOwnerField);
    user.clear(informationAssetOwnerField);
    // noinspection ES6MissingAwait
    user.type(businessOwnerField, "new business owner");
    // noinspection ES6MissingAwait
    user.type(productOwnerField, "new product owner");
    // noinspection ES6MissingAwait
    user.type(technicalOwnerField, "new technical owner");
    // noinspection ES6MissingAwait
    user.type(serviceOwnerField, "new service owner");
    // noinspection ES6MissingAwait
    user.type(informationAssetOwnerField, "new information asset owner");

    user.click(saveButton);

    await waitFor(() =>
      expect(submitHandler).toBeCalledWith({
        businessOwner: "new business owner",
        productOwner: "new product owner",
        technicalOwner: "new technical owner",
        serviceOwner: "new service owner",
        informationAssetOwner: "new information asset owner",
      })
    );
  });

  it("trims values before calling the submission handler", async () => {
    setUp();
    const businessOwnerField = screen.getByLabelText(/business owner/i);
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const technicalOwnerField = screen.getByLabelText(/technical owner/i);
    const serviceOwnerField = screen.getByLabelText(/service owner/i);
    const informationAssetOwnerField = screen.getByLabelText(
      /information asset owner/i
    );
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(businessOwnerField, "  busy owner with extra spaces  ");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(productOwnerField, "  owner with extra spaces  ");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(technicalOwnerField, "  another owner with more spaces  ");
    // noinspection ES6MissingAwait
    user.type(serviceOwnerField, "  service owner with extra spaces  ");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(
      informationAssetOwnerField,
      "  yet another owner with more spaces  "
    );
    user.click(saveButton);

    await waitFor(() =>
      expect(submitHandler).toBeCalledWith({
        businessOwner: "busy owner with extra spaces",
        productOwner: "owner with extra spaces",
        technicalOwner: "another owner with more spaces",
        serviceOwner: "service owner with extra spaces",
        informationAssetOwner: "yet another owner with more spaces",
      })
    );
  });

  it.each(["owner", null])(
    "does not send unchanged values to the submission handler",
    async (value) => {
      setUp({ product_owner: value, tech_owner: value });
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.click(saveButton);

      await waitFor(() => expect(submitHandler).toBeCalledWith({}));
    }
  );

  it("calls cancel handler", () => {
    setUp();
    const cancelButton = screen.getByRole("button", { name: /cancel/i });

    user.click(cancelButton);

    expect(cancelHandler).toBeCalled();
  });

  it.each([
    "business owner",
    "product owner",
    "technical owner",
    "service owner",
    "information asset owner",
  ])("validates %p contact before submission", async (label) => {
    setUp();
    const ownerField = screen.getByLabelText(new RegExp(label, "i"));
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(ownerField, "$");
    user.click(saveButton);

    expect(
      await screen.findByText(
        /must not use the following special characters/i,
        { selector: "label *" }
      )
    ).toBeVisible();
  });

  it("shows an error summary containing all error details", async () => {
    setUp();
    const businessOwnerField = screen.getByLabelText(/business owner/i);
    const productOwnerField = screen.getByLabelText(/product owner/i);
    const technicalOwnerField = screen.getByLabelText(/technical owner/i);
    const serviceOwnerField = screen.getByLabelText(/service owner/i);
    const informationAssetOwnerField = screen.getByLabelText(
      /information asset owner/i
    );
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(businessOwnerField, "!!!");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(technicalOwnerField, "x");
    // noinspection ES6MissingAwait
    user.type(serviceOwnerField, "x");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(productOwnerField, "!?$");
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(informationAssetOwnerField, "x");
    user.click(saveButton);

    const errors = await screen.findAllByText(/must/i, { selector: "a" });
    expect(errors).toHaveLength(5);
    expect(errors[0]).toHaveTextContent("special characters");
    expect(errors[1]).toHaveTextContent("incomplete");
    expect(errors[2]).toHaveTextContent("incomplete");
    expect(errors[3]).toHaveTextContent("special characters");
    expect(errors[4]).toHaveTextContent("incomplete");
  });

  it("shows validation errors returned from the API", async () => {
    submitHandler.mockRejectedValue(
      new ValidationError({
        businessOwner: "busy validation error",
        productOwner: "product validation error",
        technicalOwner: "tech validation error",
        serviceOwner: "service validation error",
        informationAssetOwner: "information validation error",
      })
    );
    setUp();
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(saveButton);

    expect(
      await screen.findAllByText(/validation error/i, { selector: "a" })
    ).toHaveLength(5);
  });
});
