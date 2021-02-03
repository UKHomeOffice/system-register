import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";
import { Form, Formik } from "formik";

import AliasInputList from ".";

describe("AliasInputList", () => {
  const submitHandler = jest.fn();

  function setup({ initialValues = {} }) {
    return render(
      <Formik initialValues={initialValues} onSubmit={submitHandler}>
        <Form>
          <AliasInputList />
        </Form>
      </Formik>
    );
  }

  function overtype(field, value) {
    user.clear(field);
    // noinspection JSIgnoredPromiseFromCall
    user.type(field, value);
  }

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it("displays fields with initial values", async () => {
    setup({ initialValues: { aliases: ["an alias", "another alias"] } });

    expect(await screen.findByDisplayValue("an alias")).toBeInTheDocument();
    expect(screen.getByDisplayValue("another alias")).toBeInTheDocument();
  });

  describe("removing entries", () => {
    it("removes the corresponding entry when a Remove button is clicked", async () => {
      setup({ initialValues: { aliases: ["an alias", "another alias", "yet another value"] } });
      const removeButtons = await screen.findAllByRole("button", { name: /remove/i });

      user.click(removeButtons[1]);

      await waitFor(() => {
        expect(screen.queryByDisplayValue("another alias")).not.toBeInTheDocument();
      });
      expect(screen.getByDisplayValue("an alias")).toBeInTheDocument();
      expect(screen.getByDisplayValue("yet another value")).toBeInTheDocument();
    });

    it("does not submit the form when removing an entry", async () => {
      setup({ initialValues: { aliases: [""] } });
      const removeButton = await screen.findByRole("button", { name: /remove/i });

      user.click(removeButton);

      await waitFor(() => {
        expect(screen.queryByRole("textbox")).not.toBeInTheDocument();
      });
      expect(submitHandler).not.toBeCalled();
    });
  });

  describe("adding entries", () => {
    it("adds a new, empty entry when the Add button is clicked", async () => {
      setup({ initialValues: { aliases: [] } });
      const addButton = await screen.findByRole("button", { name: /add/i });

      user.click(addButton);

      expect(await screen.findByDisplayValue("")).toBeInTheDocument();
    });

    it("does not submit the form when adding an entry", async () => {
      setup({ initialValues: { aliases: [] } });
      const add = await screen.findByRole("button", { name: /add/i });

      user.click(add);

      await screen.findByRole("textbox");
      expect(submitHandler).not.toBeCalled();
    });
  });

  describe("validation", () => {
    it("validates name before submission", async () => {
      setup({ initialValues: { aliases: ["alias"] } });
      const aliasFields = screen.getAllByRole("textbox");

      overtype(aliasFields[0], "$");
      user.tab();

      expect(await screen.findByText(/must not use the following special characters/i)).toBeInTheDocument();
    });
  });
});
