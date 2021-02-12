import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";
import { Formik } from "formik";

import ErrorSummary from "../ErrorSummary";

describe("ErrorSummary", () => {
  let originalScrollIntoView = null;

  beforeAll(() => {
    originalScrollIntoView = Element.prototype.scrollIntoView;
    Element.prototype.scrollIntoView = jest.fn();
  });

  afterAll(() => {
    Element.prototype.scrollIntoView = originalScrollIntoView;
  });

  it("displays form errors", async () => {
    render(
      <Formik
        initialErrors={{ error: "there was an error" }}
        initialTouched={{ error: true }}
        initialValues={{}}
        onSubmit={null}
      >
        <ErrorSummary />
      </Formik>
    );

    expect(
      await screen.findByText("there was an error", { selector: "a" })
    ).toBeInTheDocument();
  });

  it("displays errors for each error in an array", () => {
    render(
      withFormik(
        <ErrorSummary />,
        { many: ["error 1", "error 2", "error 3"] },
        { many: [true, false, true] }
      )
    );

    const errors = screen.getAllByText(/error/i);
    expect(errors).toHaveLength(2);
    expect(errors[0]).toHaveTextContent("error 1");
    expect(errors[1]).toHaveTextContent("error 3");
  });

  it("does not display if there are no array errors", () => {
    render(
      withFormik(<ErrorSummary />, { field: [undefined] }, { field: [true] })
    );

    expect(screen.queryByText("There is a problem")).not.toBeInTheDocument();
  });

  it("does not display if there are no errors", () => {
    render(
      <Formik initialValues={{}} onSubmit={null}>
        <ErrorSummary />
      </Formik>
    );

    expect(screen.queryByText("There is a problem")).not.toBeInTheDocument();
  });

  it("does not display errors for fields that are not showing their own errors", async () => {
    render(
      <Formik
        initialErrors={{
          error: "showing on field",
          otherError: "not yet visible",
        }}
        initialTouched={{ error: true, otherError: false }}
        initialValues={{}}
        onSubmit={null}
      >
        <ErrorSummary />
      </Formik>
    );

    expect(
      await screen.findByText("showing on field", { selector: "a" })
    ).toBeInTheDocument();
    expect(screen.queryByText("not yet visible")).not.toBeInTheDocument();
  });

  it("orders multiple errors by field", async () => {
    render(
      <Formik
        initialErrors={{
          first: "first error",
          second: "second error",
          third: ["third error"],
        }}
        initialTouched={{ first: true, second: true, third: [true] }}
        initialValues={{}}
        onSubmit={null}
      >
        <ErrorSummary order={["second", "first", "third"]} />
      </Formik>
    );

    const errors = await screen.findAllByText(/error/, { selector: "a" });
    expect(errors[0]).toHaveTextContent("second error");
    expect(errors[1]).toHaveTextContent("first error");
    expect(errors[2]).toHaveTextContent("third error");
  });

  describe("focus", () => {
    it("receives focus when shown", async () => {
      render(withFormik(<ErrorSummary />, { field: "error" }, { field: true }));

      await waitFor(() => {
        const summary = document.getElementById("error-summary");

        expect(summary).toHaveFocus();
      });
    });

    it("does not move focus if errors have not changed", async () => {
      const content = (
        <>
          <ErrorSummary />
          <input name="field" />
        </>
      );
      const { rerender } = render(
        withFormik(content, { field: "text" }, { field: true })
      );
      await waitFor(() => {
        expect(document.getElementById("error-summary")).toHaveFocus();
      });
      screen.getByRole("textbox").focus();

      rerender(withFormik(content));

      await waitFor(() => expect(screen.getByRole("textbox")).toHaveFocus());
      expect(document.getElementById("error-summary")).not.toHaveFocus();
    });

    it("moves focus to field when error is clicked", async () => {
      render(
        withFormik(
          <>
            <ErrorSummary />
            <input name="field" />
          </>,
          { field: "error text" },
          { field: true }
        )
      );
      const errorLink = await screen.findByText(/error text/i, {
        selector: "a",
      });
      const field = screen.getByRole("textbox");

      user.click(errorLink);

      await waitFor(() => expect(field).toHaveFocus());
    });

    it.each(["input", "textarea"])(
      "only moves focus to form controls: %p",
      async (control) => {
        render(
          withFormik(
            <>
              <object name="input">content</object>
              <object name="textarea">content</object>

              <ErrorSummary />

              <input name="input" defaultValue="input" />
              <textarea name="textarea" defaultValue="textarea" />
            </>,
            { input: "input", textarea: "textarea" },
            { input: true, textarea: true }
          )
        );
        const errorLink = await screen.findByText(control, {
          selector: "#error-summary a",
        });
        const field = screen.getByDisplayValue(control);

        user.click(errorLink);

        await waitFor(() => expect(field).toHaveFocus());
      }
    );
  });
});

function withFormik(component, errors = {}, touched = {}) {
  return (
    <Formik
      initialValues={{}}
      onSubmit={null}
      initialErrors={errors}
      initialTouched={touched}
    >
      {component}
    </Formik>
  );
}
