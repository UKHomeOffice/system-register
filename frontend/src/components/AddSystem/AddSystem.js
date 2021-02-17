import React from "react";
import { Form, Formik } from "formik";
import TextField from "../TextField";
import { Button } from "govuk-react";

import "./AddSystem.css";

export default function AddSystem() {
  return (
    <div className="centerContent">
      <h1>Add a system to the register</h1>
      {/*secondary class not defined in app.css - should it be?*/}
      <p className="add-system-secondary">
        Please enter the name for the new system.
      </p>

      <Formik onSubmit={() => {}} initialValues={{}}>
        <Form>
          <TextField
            name="name"
            hint="What is the primary name for the system?"
            inputClassName="add-system-width-two-thirds"
          >
            System name
          </TextField>
          <div className="add-system-form-controls">
            <Button type="submit">Save</Button>
          </div>
        </Form>
      </Formik>
    </div>
  );
}
