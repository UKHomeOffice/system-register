import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import {
  flow,
  isArray,
  isEmpty,
  map,
  mapValues,
  reject,
  trim,
  update,
} from "lodash-es";

import AliasInputList from "../../AliasInputList";
import ErrorSummary from "../../ErrorSummary/ErrorSummary";
import PageTitle from "../../PageTitle";
import SecondaryButton from "../../SecondaryButton";
import Textarea from "../../Textarea";
import TextField from "../../TextField";
import ValidationError from "../../../services/validationError";
import {
  validateName,
  validateDescription,
  validateAliases,
} from "./validators";

const deepTrim = (values) =>
  mapValues(values, (value) =>
    isArray(value) ? map(value, trim) : trim(value)
  );
const removeBlankAliases = (values) =>
  update(values, "aliases", (aliases) => reject(aliases, isEmpty));

export default function AddSystemForm({ onSubmit, onCancel, validate }) {
  const handleSubmit = useCallback(
    async (values, formik) => {
      const newValues = flow(deepTrim, removeBlankAliases)(values);
      try {
        await onSubmit(newValues);
      } catch (e) {
        if (e instanceof ValidationError) {
          formik.setErrors(e.errors);
        }
      }
    },
    [onSubmit]
  );

  const handleCancel = useCallback(() => {
    onCancel();
  }, [onCancel]);

  const validateSystemName = useCallback(
    (value) => validateName(value) || validate({ name: value }),
    [validate]
  );

  return (
    <div className="centerContent">
      <PageTitle>Add a system</PageTitle>

      <Formik
        onSubmit={handleSubmit}
        initialValues={{ name: "", description: "", aliases: [""] }}
        validateOnChange={false}
        validate={validateAliases}
      >
        <>
          <ErrorSummary order={["name", "description", "aliases"]} />

          <h1>Add a system to the register</h1>
          <p className="add-system-secondary">
            Please enter the name, description and any aliases for the new
            system.
          </p>

          <Form>
            <TextField
              name="name"
              hint="What is the primary name for the system?"
              inputClassName="add-system-width-two-thirds"
              validate={validateSystemName}
            >
              System name
            </TextField>
            <Textarea
              name="description"
              hint="Please provide a non-specialist summary of what the system is used for."
              inputClassName="width-two-thirds"
              validate={validateDescription}
            >
              System description
            </Textarea>
            <AliasInputList />
            <div className="add-system-form-controls">
              <Button type="submit">Save</Button>
              <SecondaryButton onClick={handleCancel}>Cancel</SecondaryButton>
            </div>
          </Form>
        </>
      </Formik>
    </div>
  );
}

AddSystemForm.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  validate: PropTypes.func.isRequired,
};
