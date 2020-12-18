import React from 'react'
import {queryByText, render, screen} from "@testing-library/react";
import { Formik } from "formik";

import ErrorSummary from "../ErrorSummary";

describe("ErrorSummary", () => {
  it("displays form errors", async () => {
    render(<Formik initialErrors={{error: "there was an error"}} initialValues={{}} onSubmit={null}>
    <ErrorSummary />
    </Formik>)

    expect(await screen.findByText("there was an error", {selector: "a"})).toBeInTheDocument();
  })

  it("does not display if there are no errors", () => {
    render(<Formik initialValues={{}} onSubmit={null}>
      <ErrorSummary />
    </Formik>)

    expect(screen.queryByText("There is a problem")).not.toBeInTheDocument();
  })
})
