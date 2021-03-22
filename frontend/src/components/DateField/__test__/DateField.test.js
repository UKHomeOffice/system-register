import React from "react";
import { render, screen } from "@testing-library/react";
import { Form, Formik } from "formik";

import DateField from "../index";

const submitHandler = jest.fn();

function setUp({
  initialValues = {},
  initialErrors = { testDate: undefined },
  initialTouched = { testDate: false },
}) {
  return render(
    <Formik
      initialValues={initialValues}
      onSubmit={submitHandler}
      initialErrors={initialErrors}
      initialTouched={initialTouched}
    >
      <Form>
        <DateField hintText="a hint about the field" name="testDate">
          Test date
        </DateField>
      </Form>
    </Formik>
  );
}

describe("DateField", () => {
  it("displays the field heading", () => {
    setUp({ initialValues: {} });
    const fieldHeading = screen.getByText("Test date");

    expect(fieldHeading).toBeInTheDocument();
  });

  it("displays the field hint text", () => {
    setUp({ initialValues: {} });

    const hint = screen.getByText("a hint about the field");

    expect(hint).toBeInTheDocument();
  });

  it("displays fields with initial values", () => {
    setUp({
      initialValues: { testDate: { day: "1", month: "6", year: "2021" } },
    });

    const day = screen.getByLabelText("Day");
    const month = screen.getByLabelText("Month");
    const year = screen.getByLabelText("Year");

    expect(day).toHaveValue(1);
    expect(month).toHaveValue(6);
    expect(year).toHaveValue(2021);
  });

  it("displays an error message if a validation error occurs", () => {
    setUp({
      initialValues: {},
      initialErrors: { testDate: "There is an error!" },
      initialTouched: { testDate: true },
    });
    const errorMessage = screen.getByText("There is an error!");

    expect(errorMessage).toBeInTheDocument();
  });
});
