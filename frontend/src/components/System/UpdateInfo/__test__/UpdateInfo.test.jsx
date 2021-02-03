import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import UpdateInfo from "../UpdateInfo";
import ValidationError from "../../../../services/validationError";

describe("UpdateInfo", () => {
  const submitHandler = jest.fn();
  const cancelHandler = jest.fn();

  function setUp(props = {}) {
    const actualProps = {
      system: null,
      portfolios: [],
      onSubmit: submitHandler,
      onCancel: cancelHandler,
      onBeforeNameChange: () => false,
      withAliases: true,
      ...props,
    };
    return render(<UpdateInfo {...actualProps} />);
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
    setUp({ system: { name: 'system name' } });

    expect(screen.getByRole("heading")).toHaveTextContent("system name");
  });

  it("displays a loading message if data is unavailable", () => {
    setUp({ system: null });

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
  });

  it("calls submission handler with updated values", async () => {
    setUp({ system: { name: "system name", description: "description", aliases: ["alias"] } });
    const systemNameField = screen.getByLabelText(/system name/i);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const systemAliasField = screen.getByDisplayValue("alias");
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "new system name");
    overtype(systemDescriptionField, "new system description");
    overtype(systemAliasField, "new alias");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      name: "new system name",
      description: "new system description",
      aliases: ["new alias"],
    }));
  });

  it("trims values before calling the submission handler", async () => {
    setUp({ system: { name: "system name", description: "description", aliases: ["alias"] } });
    const systemNameField = screen.getByLabelText(/system name/i);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const systemAliasField = screen.getByDisplayValue("alias");
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "    new system name with spaces ");
    overtype(systemDescriptionField, "    new system description with spaces ");
    overtype(systemAliasField, " new alias  ");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({
      name: "new system name with spaces",
      description: "new system description with spaces",
      aliases: ["new alias"],
    }));
  });

  it.each(["value", null])
  ("does not send unchanged values to the submission handler: %p", async (value) => {
    setUp({ system: { name: value, description: value, aliases: value && [value] } });
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({}));
  });

  it("does not send values to submit handler if all aliases in array are the same regardless of order", async () => {
    setUp({system: {aliases: ["alias 1", "alias 2", "alias 3"]}});
    const aliasFields = screen.getAllByDisplayValue(/alias/);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(aliasFields[0], "alias 3");
    overtype(aliasFields[1], "alias 1");
    overtype(aliasFields[2], "alias 2");
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({}));
  } )

  it.each(["", " "])
  ("does not submit blank aliases", async (value) => {
    setUp({ system: { name: "name", description: "description", aliases: [] } });
    const addAliasButton = screen.getByRole("button", { name: /add/i });
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(addAliasButton);
    overtype(screen.getAllByDisplayValue("")[0], value);
    user.click(saveButton);

    await waitFor(() => expect(submitHandler).toBeCalledWith({}));
  });

  it("provides an initial empty alias field", async () => {
    setUp({ system: { name: "name", description: "description" } });

    const aliasField = await screen.findByDisplayValue("");

    expect(aliasField).toBeInTheDocument();
  });

  it("always provides an extra empty alias field", async () => {
    setUp({ system: { name: "name", description: "description", aliases: ["alias"] } });

    const aliasField = await screen.findByDisplayValue("");

    expect(aliasField).toBeInTheDocument();
  });

  it("calls cancel handler", () => {
    setUp({ system: { name: null } });
    const cancelButton = screen.getByRole("button", { name: /cancel/i });

    user.click(cancelButton);

    expect(cancelHandler).toBeCalled();
  });

  it("validates name before submission", async () => {
    setUp({ system: { name: "system name" } });
    const systemNameField = screen.getByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "$");
    user.click(saveButton);

    expect(await screen.findByText(/must not use the following special characters/i, { selector: "label *" })).toBeInTheDocument();
  });

  it("validates description before submission", async () => {
    setUp({ system: { description: "system description "} });
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemDescriptionField, "x");
    user.click(saveButton);

    expect(await screen.findByText(/must enter a description or leave blank/i, { selector: "label *" })).toBeInTheDocument();
  });

  it("shows an error summary containing all error details", async () => {
    setUp({ system: { name: "system name", description: "system description", aliases: ["alias"] } });
    const systemNameField = screen.getByLabelText(/system name/i);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const aliasField = screen.getByDisplayValue("alias");
    const saveButton = screen.getByRole("button", { name: /save/i });

    overtype(systemNameField, "$");
    overtype(systemDescriptionField, "x");
    overtype(aliasField, "!");
    user.click(saveButton);

    const errors = await screen.findAllByText(/must/i, { selector: "a" });
    expect(errors).toHaveLength(3);
    expect(errors[0]).toHaveTextContent(/must not use the following special characters/i);
    expect(errors[1]).toHaveTextContent(/must enter a description or leave blank/i);
    expect(errors[2]).toHaveTextContent(/must not use the following special characters/i);
  });

  it("shows validation errors returned from the API", async () => {
    submitHandler.mockRejectedValue(new ValidationError({
      name: "name validation error",
      description: "description validation error",
      aliases: "alias validation error",
    }));
    setUp({ system: { name: 'system name' } });
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(saveButton);

    const errors = await screen.findAllByText(/validation error/i, { selector: "a" });
    expect(errors).toHaveLength(3);
    expect(errors[0]).toHaveTextContent("name validation error");
    expect(errors[1]).toHaveTextContent("description validation error");
    expect(errors[2]).toHaveTextContent("alias validation error");

    expect(screen.getByText("alias validation error", { selector: ".alias-input-list *" })).toBeInTheDocument();
  });
});
