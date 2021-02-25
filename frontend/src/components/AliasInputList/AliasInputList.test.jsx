import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";
import { Form, Formik } from "formik";

import AliasInputList from "./index";

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
      setup({
        initialValues: {
          aliases: ["an alias", "another alias", "yet another value"],
        },
      });
      const removeButtons = await screen.findAllByRole("button", {
        name: /remove/i,
      });

      user.click(removeButtons[1]);

      await waitFor(() => {
        expect(
          screen.queryByDisplayValue("another alias")
        ).not.toBeInTheDocument();
      });
      expect(screen.getByDisplayValue("an alias")).toBeInTheDocument();
      expect(screen.getByDisplayValue("yet another value")).toBeInTheDocument();
    });

    it("does not submit the form when removing an entry", async () => {
      setup({ initialValues: { aliases: [""] } });
      const removeButton = await screen.findByRole("button", {
        name: /remove/i,
      });

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

  describe("moving focus", () => {
    it("gives focus to the Add button when the only remaining input is removed", async () => {
      setup({ initialValues: { aliases: [""] } });
      const removeButton = await screen.findByRole("button", {
        name: /remove/i,
      });

      user.click(removeButton);

      const addButton = await screen.findByRole("button", { name: /add/i });
      expect(addButton).toHaveFocus();
    });

    it("gives focus to the next text field when an input (other than the last) is removed", async () => {
      setup({ initialValues: { aliases: ["first", "second"] } });
      const removeButtons = await screen.findAllByRole("button", {
        name: /remove/i,
      });

      user.click(removeButtons[0]);

      await waitFor(() => {
        const nextField = screen.getByDisplayValue("second");
        expect(nextField).toHaveFocus();
      });
    });

    it("gives focus to the previous text field when the last input is removed", async () => {
      setup({ initialValues: { aliases: ["first", "second"] } });
      const removeButtons = await screen.findAllByRole("button", {
        name: /remove/i,
      });

      user.click(removeButtons[1]);

      await waitFor(() => {
        const nextField = screen.getByDisplayValue("first");
        expect(nextField).toHaveFocus();
      });
    });
  });
});
