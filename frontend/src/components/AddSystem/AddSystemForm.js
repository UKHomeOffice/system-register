import { Form, Formik } from "formik";
import TextField from "../TextField";
import { Button } from "govuk-react";
import React from "react";
import PropTypes from "prop-types";
import { mapValues } from "lodash-es";
import { validateName } from "../System/UpdateInfo/validators";
import ValidationError from "../../services/validationError";
import ErrorSummary from "../ErrorSummary/ErrorSummary";

export default function AddSystemForm({ onSubmit, onBeforeNameChange }) {
  const handleSubmit = async (values, formik) => {
    const trimmedValues = mapValues(values, (value) => value.trim());
    try {
      await onSubmit(trimmedValues);
    } catch (e) {
      if (e instanceof ValidationError) {
        formik.setErrors(e.errors);
      }
    }
  };

  return (
    <div className="centerContent">
      <Formik
        onSubmit={handleSubmit}
        initialValues={{}}
        validateOnChange={false}
      >
        <>
          <ErrorSummary order={["name"]} />
          <h1>Add a system to the register</h1>
          {/*secondary class not defined in app.css - should it be?*/}
          <p className="add-system-secondary">
            Please enter the name for the new system.
          </p>
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
        </>
      </Formik>
    </div>
  );
}

AddSystemForm.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  onBeforeNameChange: PropTypes.func.isRequired,
};
