import React from "react";
import user from "@testing-library/user-event";
import { render, screen } from "@testing-library/react";
import { Form, Formik } from "formik";

import DateField from "../index";

const submitHandler = jest.fn();

function setUp({
  initialValues = {},
  initialErrors = { testDate: undefined },
  initialTouched = { testDate: false },
  validate = () => {},
}) {
  return render(
    <Formik
      initialValues={initialValues}
      onSubmit={submitHandler}
      initialErrors={initialErrors}
      initialTouched={initialTouched}
    >
      <Form>
        <DateField
          hintText="a hint about the field"
          name="testDate"
          validate={validate}
        >
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

  it("displays an error message if a validation error occurs", async () => {
    setUp({
      initialValues: { testDate: { day: "1", month: "1", year: "2000" } },
    });
    const dayField = screen.getByLabelText("Day");

    // noinspection ES6MissingAwait
    user.type(dayField, "55");
    dayField.blur();

    const errorMessage = await screen.findByText(/invalid/);
    expect(errorMessage).toBeInTheDocument();
  });

  it("displays an error message if a custom validator returns an error", async () => {
    setUp({
      initialValues: { testDate: { day: "1", month: "1", year: "2000" } },
      validate: () => "an error",
    });
    const dayField = screen.getByLabelText("Day");

    dayField.focus();
    dayField.blur();

    const errorMessage = await screen.findByText(/an error/);
    expect(errorMessage).toBeInTheDocument();
  });
});
