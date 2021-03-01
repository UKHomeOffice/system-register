import React from "react";
import { act, render } from "@testing-library/react";
import { Formik } from "formik";

import { FormikAwarePageTitle } from ".";

const renderWithFormik = (component, errors = {}, options = {}) => {
  const ref = React.createRef();
  const componentWithFormik = (
    <Formik
      innerRef={ref}
      initialValues={{}}
      initialErrors={errors}
      onSubmit={() => {}}
    >
      {component}
    </Formik>
  );
  const renderResult = render(componentWithFormik, options);
  return { ...renderResult, ref };
};

describe("FormikAwarePageTitle", () => {
  it("changes the title", () => {
    renderWithFormik(<FormikAwarePageTitle>The title</FormikAwarePageTitle>);

    expect(document.title).toEqual("The title — System Register");
  });

  it("indicates a form error occurred in the title", () => {
    renderWithFormik(<FormikAwarePageTitle>Some text</FormikAwarePageTitle>, {
      field: "is invalid",
    });

    expect(document.title).toEqual("Error: Some text — System Register");
  });

  it("removes the error indicator when the error is resolved", () => {
    const { ref } = renderWithFormik(
      <FormikAwarePageTitle>Some text</FormikAwarePageTitle>,
      { field: "is invalid" }
    );

    act(() => {
      ref.current.setErrors({});
    });

    expect(document.title).toEqual("Some text — System Register");
  });
});
