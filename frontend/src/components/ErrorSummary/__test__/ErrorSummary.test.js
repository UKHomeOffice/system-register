import React from "react";
import { render, screen } from "@testing-library/react";
import { Formik } from "formik";

import ErrorSummary from "../ErrorSummary";

describe("ErrorSummary", () => {
  it("displays form errors", async () => {
    render(
      <Formik initialErrors={{ error: "there was an error" }}
              initialTouched={{ error: true }}
              initialValues={{}}
              onSubmit={null}>
        <ErrorSummary />
      </Formik>
    );

    expect(await screen.findByText("there was an error", { selector: "a" })).toBeInTheDocument();
  });

  it("does not display if there are no errors", () => {
    render(
      <Formik initialValues={{}} onSubmit={null}>
        <ErrorSummary />
      </Formik>
    );

    expect(screen.queryByText("There is a problem")).not.toBeInTheDocument();
  });

  it("does not display errors for fields that not be showing their own errors", async () => {
    render(
      <Formik initialErrors={{ error: "showing on field", otherError: "not yet visible" }}
              initialTouched={{ error: true, otherError: false }}
              initialValues={{}}
              onSubmit={null}>
        <ErrorSummary />
      </Formik>
    );

    expect(await screen.findByText("showing on field", { selector: "a" })).toBeInTheDocument();
    expect(screen.queryByText("not yet visible")).not.toBeInTheDocument();
  });

  it("orders multiple errors by field", async () => {
    render(
      <Formik initialErrors={{ first: "first error", second: "second error" }}
              initialTouched={{ first: true, second: true }}
              initialValues={{}}
              onSubmit={null}>
        <ErrorSummary order={["second", "first"]}/>
      </Formik>
    );

    const errors = await screen.findAllByText(/error/, { selector: "a" });
    expect(errors[0]).toHaveTextContent("second error");
    expect(errors[1]).toHaveTextContent("first error");
  });
});
