import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor, within } from "@testing-library/react";

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

function getAliasFields() {
  const aliasesContainer = screen
    .getByText("Aliases")
    .closest(".alias-input-list");
  return within(aliasesContainer).getAllByRole("textbox");
}

describe("add system", () => {
  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("displays an initially empty system name field", () => {
    setUp();
    const systemNameField = screen.getByLabelText(/system name/i);

    expect(systemNameField).toBeInTheDocument();
    expect(systemNameField.value).toBe("");
  });

  it("displays an initially empty system description field", () => {
    setUp();
    const systemDescriptionField = screen.getByLabelText(/system description/i);

    expect(systemDescriptionField).toBeInTheDocument();
    expect(systemDescriptionField.value).toBe("");
  });

  it("displays a single, initially empty alias input field", () => {
    setUp();
    const aliasInputFields = getAliasFields();

    expect(aliasInputFields.length).toBe(1);
    expect(aliasInputFields[0].value).toBe("");
  });

  it("calls submission handler with new values for system name and description", async () => {
    setUp();
    const systemNameField = await screen.findByLabelText(/system name/i);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const aliasInputFields = getAliasFields();
    const saveButton = await screen.findByText(/save/i);

    overtype(systemNameField, "new system name");
    overtype(systemDescriptionField, "new system description");
    overtype(aliasInputFields[0], "new system alias");
    user.click(saveButton);

    await waitFor(() => {
      expect(submitHandler).toBeCalledWith({
        name: "new system name",
        description: "new system description",
        aliases: ["new system alias"],
      });
    });
  });

  it("trims values before calling the submission handler", async () => {
    setUp();
    const systemNameField = await screen.findByLabelText(/system name/i);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const aliasInputFields = getAliasFields();
    const saveButton = await screen.findByText(/save/i);

    overtype(systemNameField, "       new system name       ");
    overtype(systemDescriptionField, "     new system description     ");
    overtype(aliasInputFields[0], "     new system alias    ");
    user.click(saveButton);

    await waitFor(() => {
      expect(submitHandler).toBeCalledWith({
        name: "new system name",
        description: "new system description",
        aliases: ["new system alias"],
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

  it("validates alias for invalid values before submission", async () => {
    setUp();
    const aliasInputFields = getAliasFields();

    overtype(aliasInputFields[0], "$");
    user.tab();

    expect(
      await screen.findByText(
        /must not use the following special characters/i,
        { selector: ".alias-input-list *" }
      )
    ).toBeInTheDocument();
  });

  it("validates aliases for duplicates before submission", async () => {
    setUp();
    const addAliasButton = screen.getByRole("button", {
      name: /add another alias/i,
    });
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(addAliasButton);
    const aliasInputFields = getAliasFields();

    overtype(aliasInputFields[0], "alias");
    overtype(aliasInputFields[1], "alias");
    user.click(saveButton);

    expect(
      await screen.findAllByText(/duplicate alias/, {
        selector: ".alias-input-list *",
      })
    ).toHaveLength(2);
  });

  it("revalidates duplicates after one of the duplicates has been removed", async () => {
    setUp();
    const addAliasButton = screen.getByRole("button", {
      name: /add another alias/i,
    });
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(addAliasButton);
    const aliasInputFields = getAliasFields();

    overtype(aliasInputFields[0], "alias");
    overtype(aliasInputFields[1], "alias");
    user.click(saveButton);

    expect(await screen.findAllByText(/duplicate alias/)).not.toHaveLength(0);

    const removeButtons = screen.getAllByRole("button", { name: /remove/i });
    user.click(removeButtons[0]);

    await waitFor(() => {
      expect(screen.queryByText(/duplicate alias/i)).not.toBeInTheDocument();
    });
  });

  it.each(["", " "])("does not submit blank aliases", async (value) => {
    setUp();
    const aliasInputFields = getAliasFields();
    const systemNameField = screen.getByLabelText(/system name/i);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "irrelevant");
    overtype(systemDescriptionField, "irrelevant");
    overtype(aliasInputFields[0], value);
    user.click(saveButton);

    await waitFor(() =>
      expect(submitHandler).toBeCalledWith({
        name: "irrelevant",
        description: "irrelevant",
        aliases: [],
      })
    );
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
