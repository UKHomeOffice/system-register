import React from "react";
import { Form, Formik } from "formik";
import TextField from "../TextField";
import { Button } from "govuk-react";

import "./AddSystem.css";
import PropTypes from "prop-types";

export default function AddSystem({ onSubmit }) {
  const handleSubmit = async (values) => {
    await onSubmit(values);
  };

  return (
    <div className="centerContent">
      <h1>Add a system to the register</h1>
      {/*secondary class not defined in app.css - should it be?*/}
      <p className="add-system-secondary">
        Please enter the name for the new system.
      </p>

      <Formik onSubmit={handleSubmit} initialValues={{}}>
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

AddSystem.propTypes = {
  onSubmit: PropTypes.func.isRequired,
};
