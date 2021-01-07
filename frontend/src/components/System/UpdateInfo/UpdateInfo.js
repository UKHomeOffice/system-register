import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { mapValues, omitBy } from "lodash-es";

import ErrorSummary from "../../ErrorSummary/ErrorSummary";
import Textarea from "../../Textarea";
import TextField from "../../TextField";
import ValidationError from "../../../services/validationError";
import { validateName, validateDescription } from "./validators";

import "./UpdateInfo.css";

const emptyIfUndefined = (value) => value != null ? value : "";

const infoAbout = (system) => ({
  name: emptyIfUndefined(system.name),
  description: emptyIfUndefined(system.description),
});

function UpdateInfo({ system, onSubmit, onCancel, executeCheck, withDescription = false }) {
  const handleSubmit = useCallback(async (values, formik) => {
    const initialInfo = infoAbout(system);
    const changedInfo = omitBy(
      mapValues(values, (value) => value.trim()),
      (value, key) => value === initialInfo[key]);

    try {
      await onSubmit(changedInfo);
    } catch (e) {
      if (e instanceof ValidationError) {
        formik.setErrors(e.errors);
      }
    }
  }, [system, onSubmit]);

  const handleCancel = () => {
    onCancel();
  };

  return (
    <div className="centerContent">
      {system ? (
        <Formik
          initialValues={infoAbout(system)}
          validateOnChange={false}
          onSubmit={handleSubmit}
        >
          <>
            <ErrorSummary order={["name", "description"]} />

            <h1>{system.name}</h1>
            <p className="secondary">
              You can change the name of the system and its description
            </p>

            <Form>
              <TextField
                name="name"
                hint="Please enter the new system name"
                inputClassName="width-two-thirds"
                validate={(value) => {
                  return validateName(value, executeCheck, emptyIfUndefined(system.name))
                }}
              >
                System name
              </TextField>

              {withDescription && <Textarea
                name="description"
                hint="Please provide a brief summary description of the system"
                inputClassName="width-two-thirds"
                validate={validateDescription}
              >
                System description
              </Textarea>}

              <div className="form-controls">
                <Button type="submit">Save</Button>
                <Button
                  type="button"
                  onClick={handleCancel}
                  buttonColour="#f3f2f1"
                  buttonTextColour="#0b0c0c"
                >
                  Cancel
                </Button>
              </div>
            </Form>
          </>
        </Formik>
      ) : (
        <p>Loading system data...</p>
      )}
    </div>
  );
}

export default UpdateInfo;
