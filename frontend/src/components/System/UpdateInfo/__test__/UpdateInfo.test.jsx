import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import UpdateInfo from "../UpdateInfo";
import ValidationError from "../../../../services/validationError";

describe("UpdateInfo", () => {
  const submitHandler = jest.fn();

  function setUp(props = {}) {
    const actualProps = {
      system: null,
      portfolios: [],
      onSubmit: submitHandler,
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
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} />);

    expect(screen.getByRole("heading")).toHaveTextContent("system name");
  });

  it("displays a loading message if data is unavailable", () => {
    render(<UpdateInfo system={null} onSubmit={submitHandler} />);

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
    const cancelHandler = jest.fn();
    render(<UpdateInfo system={{ name: null }} onCancel={cancelHandler} onBeforeNameChange={() => false} />);
    const cancelButton = screen.getByRole("button", { name: /cancel/i });

    user.click(cancelButton);

    expect(cancelHandler).toBeCalled();
  });

  it("validates name before submission", async () => {
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} onBeforeNameChange={() => false} />);
    const systemNameField = screen.getByLabelText(/system name/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(systemNameField, "$");
    user.click(saveButton);

    expect(await screen.findByText(/must not use the following special characters/i, { selector: "label *" })).toBeInTheDocument();
  });

  it("validates description before submission", async () => {
    render(<UpdateInfo system={{ description: "system description" }} onSubmit={submitHandler} onBeforeNameChange={() => false} />);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.clear(systemDescriptionField);
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(systemDescriptionField, "x");
    user.click(saveButton);

    expect(await screen.findByText(/must enter a description or leave blank/i, { selector: "label *" })).toBeInTheDocument();
  });

  it("shows an error summary containing all error details", async () => {
    const system = { name: "system name", description: "system description" };
    render(<UpdateInfo system={system} onSubmit={submitHandler} onBeforeNameChange={() => false} />);
    const systemNameField = screen.getByLabelText(/system name/i);
    const systemDescriptionField = screen.getByLabelText(/system description/i);
    const saveButton = screen.getByRole("button", { name: /save/i });

    // noinspection ES6MissingAwait: there is no typing delay
    user.type(systemNameField, "$");
    user.clear(systemDescriptionField);
    // noinspection ES6MissingAwait: there is no typing delay
    user.type(systemDescriptionField, "x");
    user.click(saveButton);

    const errors = await screen.findAllByText(/must/i, { selector: "a" });
    expect(errors).toHaveLength(2);
    expect(errors[0]).toHaveTextContent(/must not use the following special characters/i);
    expect(errors[1]).toHaveTextContent(/must enter a description or leave blank/i);
  });

  it("shows validation errors returned from the API", async () => {
    submitHandler.mockRejectedValue(new ValidationError({
      name: "validation error",
      description: "validation error",
    }));
    render(<UpdateInfo system={{ name: 'system name' }} onSubmit={submitHandler} onBeforeNameChange={() => false} />);
    const saveButton = screen.getByRole("button", { name: /save/i });

    user.click(saveButton);

    const errors = await screen.findAllByText(/validation error/i, { selector: "a" });
    expect(errors).toHaveLength(2);
    expect(errors[0]).toBeInTheDocument();
    expect(errors[1]).toBeInTheDocument();
  });
});
