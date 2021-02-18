import { Form, Formik } from "formik";
import TextField from "../TextField";
import { Button } from "govuk-react";
import React from "react";
import PropTypes from "prop-types";
import { mapValues } from "lodash-es";
import { validateName } from "../System/UpdateInfo/validators";

export default function AddSystemForm({ onSubmit, onBeforeNameChange }) {
  const handleSubmit = async (values) => {
    const trimmedValues = mapValues(values, (value) => value.trim());
    await onSubmit(trimmedValues);
  };

  return (
    <Formik onSubmit={handleSubmit} initialValues={{}}>
      <Form>
        <TextField
          name="name"
          hint="What is the primary name for the system?"
          inputClassName="add-system-width-two-thirds"
          validate={(value) => {
            return validateName(value, onBeforeNameChange);
          }}
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
  onBeforeNameChange: PropTypes.func.isRequired,
};
