import React from "react";
import { render, screen } from "@testing-library/react";
import { Form, Formik } from "formik";

import DateField from "../index";

const submitHandler = jest.fn();

function setup({ initialValues = {} }) {
  return render(
    <Formik initialValues={initialValues} onSubmit={submitHandler}>
      <Form>
        <DateField hintText="a hint about the field" name="test_date">
          Test date
        </DateField>
      </Form>
    </Formik>
  );
}

describe("DateField", () => {
  it("displays the field heading", () => {
    setup({ initialValues: {} });
    const fieldHeading = screen.getByText("Test date");

    expect(fieldHeading).toBeInTheDocument();
  });

  it("displays the field hint text", () => {
    setup({ initialValues: {} });

    const hint = screen.getByText("a hint about the field");

    expect(hint).toBeInTheDocument();
  });

  it("displays fields with initial values", () => {
    setup({
      initialValues: { test_date: { day: "1", month: "6", year: "2021" } },
    });

    const day = screen.getByLabelText("Day");
    const month = screen.getByLabelText("Month");
    const year = screen.getByLabelText("Year");

    expect(day).toHaveValue(1);
    expect(month).toHaveValue(6);
    expect(year).toHaveValue(2021);
  });
});
