import React from "react";
import user from "@testing-library/user-event";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { Formik } from "formik";

import Textarea from ".";

describe("Textarea", () => {
  it("displays a label", () => {
    renderWithFormik(<Textarea name="field">label text</Textarea>);

    expect(screen.getByLabelText(/label text/)).toBeInTheDocument();
  });

  it("displays placeholder text if provided", () => {
    renderWithFormik(<Textarea name="field" placeholder="Unknown" />);

    expect(screen.getByPlaceholderText("Unknown")).toBeInTheDocument();
  });

  it("shows an error message if the value is invalid", async () => {
    renderWithFormik(<Textarea name="field" validate={() => "error message"} />, { field: "" });
    const field = screen.getByRole("textbox");

    // noinspection ES6MissingAwait: not using typing delay
    user.type(field, "invalid value");
    fireEvent.blur(field);

    await waitFor(() => {
      expect(screen.getByText("error message")).toBeInTheDocument();
    });
  });

  describe("field value", () => {
    it("initialises the field with existing values", () => {
      renderWithFormik(<Textarea name="field-name" />, { "field-name": "value" });

      expect(screen.getByDisplayValue("value")).toBeInTheDocument();
    });

    it.each([null, undefined])
    ("leaves the field blank if no value is specified", (value) => {
      renderWithFormik(<Textarea name="field" />, { "field": value });

      expect(screen.getByDisplayValue("")).toBeInTheDocument();
    });
  });

  describe("hint text", () => {
    it("displays value when available", () => {
      renderWithFormik(<Textarea name="field" hint="some hints" />)

      expect(screen.getByText("some hints")).toBeInTheDocument();
    });

    it.each([null, undefined])
    ("omits the hint when not provided", (hint) => {
      renderWithFormik(<Textarea name="field" hint={hint} />)

      expect(screen.queryByText(`${hint}`)).not.toBeInTheDocument();
    });
  });
});

function renderWithFormik(component, values = {}) {
  return render(
    <Formik initialValues={values} onSubmit={null}>
      {component}
    </Formik>
  );
}
