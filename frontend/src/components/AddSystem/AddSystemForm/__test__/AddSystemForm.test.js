import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import AddSystemForm from "../AddSystemForm";
import ValidationError from "../../../../services/validationError";

function setUp() {
  render(
    <AddSystemForm
      onSubmit={submitHandler}
      onCancel={cancelHandler}
      validate={validateName}
    />
  );
}

function overtype(field, value) {
  user.clear(field);
  // noinspection JSIgnoredPromiseFromCall
  user.type(field, value);
}

const submitHandler = jest.fn();
const cancelHandler = jest.fn();
const validateName = jest.fn();

describe("add system", () => {
  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("provides an initially empty system name field", async () => {
    setUp();
    const systemNameField = await screen.findByLabelText(/system name/i);

    expect(systemNameField).toBeInTheDocument();
    expect(systemNameField.value).toBe("");
  });

  it("provides an initially empty system description field", async () => {
    setUp();
    const systemDescriptionField = screen.getByLabelText(/system description/i);

    expect(systemDescriptionField).toBeInTheDocument();
    expect(systemDescriptionField.value).toBe("");
  });

  it("calls submission handler with new values for system name and description", async () => {
    setUp();
    const systemNameField = await screen.findByLabelText(/system name/i);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const saveButton = await screen.findByText(/save/i);

    overtype(systemNameField, "new system name");
    overtype(systemDescriptionField, "new system description");
    user.click(saveButton);

    await waitFor(() => {
      expect(submitHandler).toBeCalledWith({
        name: "new system name",
        description: "new system description",
      });
    });
  });

  it("trims values before calling the submission handler", async () => {
    setUp();
    const systemNameField = await screen.findByLabelText(/system name/i);
    const saveButton = await screen.findByText(/save/i);

    overtype(systemNameField, "       new system name       ");
    user.click(saveButton);

    await waitFor(() => {
      expect(submitHandler).toBeCalledWith({
        name: "new system name",
        description: "",
      });
    });
  });

  it("validates name before submission", async () => {
    setUp();
    const systemNameField = screen.getByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "$");
    user.click(saveButton);

    expect(
      await screen.findByText(
        /must not use the following special characters/i,
        { selector: "label *" }
      )
    ).toBeInTheDocument();
  });

  it("verifies new system with given validation function", async () => {
    validateName.mockReturnValue("system already exists");
    setUp();
    const systemNameField = screen.getByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "name");
    user.click(saveButton);

    expect(
      await screen.findByText("system already exists", { selector: "label *" })
    ).toBeInTheDocument();
  });

  it("validates description before submission", async () => {
    setUp();
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemDescriptionField, "x");
    user.click(saveButton);

    expect(
      await screen.findByText(/must enter a description or leave blank/i, {
        selector: "label *",
      })
    ).toBeInTheDocument();
  });

  it("shows an error summary containing all error details", async () => {
    setUp();
    const systemNameField = await screen.findByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "$");
    user.click(saveButton);

    const errors = await screen.findByText(/must/i, { selector: "a" });
    expect(errors).toBeInTheDocument();
    expect(errors).toHaveTextContent(
      /must not use the following special characters/i
    );
  });

  it("shows validation errors returned from the API", async () => {
    submitHandler.mockRejectedValue(
      new ValidationError({
        name: "name validation error",
      })
    );
    setUp();
    const saveButton = screen.getByRole("button", { name: /save/i });
    const systemNameField = await screen.findByLabelText(/system name/i);

    overtype(systemNameField, "valid name");
    user.click(saveButton);

    const error = await screen.findByText(/validation error/i, {
      selector: "a",
    });
    expect(error).toBeInTheDocument();
    expect(error).toHaveTextContent("name validation error");
  });

  it("calls cancel handler", () => {
    setUp();
    const cancelButton = screen.getByRole("button", { name: /cancel/i });

    user.click(cancelButton);

    expect(cancelHandler).toBeCalled();
  });
});
