import React from "react";
import { render, screen, waitFor } from "@testing-library/react";
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
        <ErrorSummary order={["second", "first"]} />
      </Formik>
    );

    const errors = await screen.findAllByText(/error/, { selector: "a" });
    expect(errors[0]).toHaveTextContent("second error");
    expect(errors[1]).toHaveTextContent("first error");
  });

  describe("focus", () => {
    it("receives focus when shown", async () => {
      render(withFormik(<ErrorSummary />,
        { field: "error" },
        { field: true }));

      await waitFor(() => {
        const summary = document.getElementById("error-summary");

        expect(summary).toHaveFocus();
      });
    });

    it("does not move focus if errors have not changed", async () => {
      const content = <>
        <ErrorSummary />
        <input name="field" />
      </>;
      const { rerender } = render(withFormik(content, { field: "text" }, { field: true }));
      await waitFor(() => {
        expect(document.getElementById("error-summary")).toHaveFocus();
      });
      screen.getByRole("textbox").focus();

      rerender(withFormik(content));

      await waitFor(() => expect(screen.getByRole("textbox")).toHaveFocus());
      expect(document.getElementById("error-summary")).not.toHaveFocus();
    });
  });
});

function withFormik(component, errors = {}, touched = {}) {
  return (
    <Formik initialValues={{}} onSubmit={null}
            initialErrors={errors} initialTouched={touched}
    >
      {component}
    </Formik>
  );
}
