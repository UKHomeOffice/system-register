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
    expect(document.title).toBe("Loading system... — System Register");
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

    overtype(businessOwnerField, "new business owner");
    overtype(productOwnerField, "new product owner");
    overtype(technicalOwnerField, "new technical owner");
    overtype(serviceOwnerField, "new service owner");
    overtype(informationAssetOwnerField, "new information asset owner");

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

    overtype(businessOwnerField, "  busy owner with extra spaces  ");
    overtype(productOwnerField, "  owner with extra spaces  ");
    overtype(technicalOwnerField, "  another owner with more spaces  ");
    overtype(serviceOwnerField, "  service owner with extra spaces  ");
    overtype(
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

    overtype(ownerField, "$");
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

    overtype(businessOwnerField, "!!!");
    overtype(technicalOwnerField, "x");
    overtype(serviceOwnerField, "x");
    overtype(productOwnerField, "!?$");
    overtype(informationAssetOwnerField, "x");
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

  it("indicates there was an error in the title", async () => {
    setUp();
    const businessOwnerField = screen.getByLabelText(/business owner/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(businessOwnerField, "!");
    user.click(saveButton);

    await waitFor(() => {
      expect(document.title).toEqual(expect.stringMatching(/^Error\b/));
    });
  });
});
