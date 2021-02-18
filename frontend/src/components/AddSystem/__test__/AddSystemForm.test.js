import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import AddSystemForm from "../AddSystemForm";

function overtype(field, value) {
  user.clear(field);
  // noinspection JSIgnoredPromiseFromCall
  user.type(field, value);
}

const submitHandler = jest.fn();
const onBeforeNameChange = jest.fn();

describe("add system", () => {
  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("provides an initial empty system name field", async () => {
    render(
      <AddSystemForm
        onSubmit={submitHandler}
        onBeforeNameChange={onBeforeNameChange}
      />
    );

    const systemNameField = await screen.findByLabelText(/system name/i);

    expect(systemNameField).toBeInTheDocument();
    expect(systemNameField.value).toBe("");
  });

  it("calls submission handler with new system name", async () => {
    render(
      <AddSystemForm
        onSubmit={submitHandler}
        onBeforeNameChange={onBeforeNameChange}
      />
    );
    const systemNameField = await screen.findByLabelText(/system name/i);
    const saveButton = await screen.findByText(/save/i);

    overtype(systemNameField, "new system name");
    user.click(saveButton);

    await waitFor(() => {
      expect(submitHandler).toBeCalledWith({
        name: "new system name",
      });
    });
  });

  it("trims values before calling the submission handler", async () => {
    render(
      <AddSystemForm
        onSubmit={submitHandler}
        onBeforeNameChange={onBeforeNameChange}
      />
    );
    const systemNameField = await screen.findByLabelText(/system name/i);
    const saveButton = await screen.findByText(/save/i);

    overtype(systemNameField, "       new system name       ");
    user.click(saveButton);

    await waitFor(() => {
      expect(submitHandler).toBeCalledWith({
        name: "new system name",
      });
    });
  });

  //why does this pass?
  it.each([" ", ""])(
    "does not send empty values to the submission handler: %p",
    async (value) => {
      render(
        <AddSystemForm
          onSubmit={submitHandler}
          onBeforeNameChange={onBeforeNameChange}
        />
      );
      const systemNameField = await screen.findByLabelText(/system name/i);
      const saveButton = await screen.findByText(/save/i);

      overtype(systemNameField, value);
      user.click(saveButton);

      await waitFor(() => {
        expect(submitHandler).not.toBeCalled();
      });
    }
  );

  it("validates name before submission", async () => {
    render(
      <AddSystemForm
        onSubmit={submitHandler}
        onBeforeNameChange={onBeforeNameChange}
      />
    );
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
});
