import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";
import { Formik } from "formik";

import AliasInputList from ".";

// TODO:
//    Validation
//    Add button
//    Styling (GDS input field)

describe("AliasInputList", () => {
  const submitHandler = jest.fn();

  function setup({ initialValues = {} }) {
    return render(
      <Formik initialValues={initialValues} onSubmit={submitHandler}>
        <AliasInputList />
      </Formik>
    );
  }

  it("displays fields with initial values", async () => {
    setup({ initialValues: { aliases: ["an alias", "another alias"] } });

    expect(await screen.findByDisplayValue("an alias")).toBeInTheDocument();
    expect(screen.getByDisplayValue("another alias")).toBeInTheDocument();
  });

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
});
