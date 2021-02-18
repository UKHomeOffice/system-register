import { Form, Formik } from "formik";
import TextField from "../TextField";
import { Button } from "govuk-react";
import React from "react";
import PropTypes from "prop-types";

export default function AddSystemForm({ onSubmit }) {
  const handleSubmit = async (values) => {
    await onSubmit(values);
  };

  return (
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
  );
}

AddSystemForm.propTypes = {
  onSubmit: PropTypes.func.isRequired,
};
