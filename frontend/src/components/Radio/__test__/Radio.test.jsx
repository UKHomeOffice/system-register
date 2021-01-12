import React from "react";
import { render, screen } from "@testing-library/react";
import { Formik } from "formik";

import Radio from "../Radio";

describe("Radio", () => {
  it("displays a label", () => {
    renderWithFormik(<Radio>label text</Radio>);

    expect(screen.getByLabelText(/label text/)).toBeInTheDocument();
  });

it("displays hint text if hint provided as prop", () => {
    renderWithFormik(<Radio hint="some hint">label text</Radio>);

    expect(screen.getByText(/some hint/)).toBeInTheDocument();
  });
});

function renderWithFormik(component, values = {}) {
  return render(
    <Formik initialValues={values} onSubmit={null}>
      {component}
    </Formik>
  );
}
