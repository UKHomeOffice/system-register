import React from "react";
import { render, screen } from "@testing-library/react";
import { Formik } from "formik";

import TextField from "../TextField";

describe("TextField", () => {
  it("displays a label", () => {
    renderWithFormik(<TextField>label text</TextField>);

    expect(screen.getByLabelText(/label text/)).toBeInTheDocument();
  });

  describe("field value", () => {
    it("initialises the field with existing values", () => {
      renderWithFormik(<TextField name="field-name" />, { "field-name": "value" });

      expect(screen.getByDisplayValue("value")).toBeInTheDocument();
    });

    it.each([null, undefined])
    ("leaves the field blank if no value is specified", (value) => {
      renderWithFormik(<TextField name="field" />, { "field": value });

      expect(screen.getByDisplayValue("")).toBeInTheDocument();
    });
  });

  describe("hint text", () => {
    it("displays value when available", () => {
      renderWithFormik(<TextField hint="some hints" />)

      expect(screen.getByText("some hints")).toBeInTheDocument();
    });

    it.each([null, undefined])
    ("omits the hint when not provided", (hint) => {
      renderWithFormik(<TextField hint={hint} />)

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
