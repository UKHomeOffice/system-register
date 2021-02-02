import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { flow, isArray, isEmpty, isEqual, map, mapValues, omitBy, reject, trim, update } from "lodash-es";

import AliasInputList from "./AliasInputList";
import ErrorSummary from "../../ErrorSummary/ErrorSummary";
import Textarea from "../../Textarea";
import TextField from "../../TextField";
import ValidationError from "../../../services/validationError";
import { validateDescription, validateName } from "./validators";

import "./UpdateInfo.css";

const emptyIfUndefined = (value) => value != null ? value : "";
const emptyArrayIfUndefined = (value) => value != null ? value : [];
const deepTrim = (values) => mapValues(values, (value) => isArray(value) ? map(value, trim) : trim(value));
const removeBlankAliases = (values) => update(values, "aliases", (aliases) => reject(aliases, isEmpty));
const removeUnchangedValues = (initialValues) => (values) => omitBy(values, (value, key) => isEqual(value, initialValues[key]));

const infoAbout = (system) => ({
  name: emptyIfUndefined(system.name),
  description: emptyIfUndefined(system.description),
  aliases: [...emptyArrayIfUndefined(system.aliases), ""],
});

function UpdateInfo({ system, onSubmit, onCancel, onBeforeNameChange, withAliases = false }) {
  const handleSubmit = useCallback(async (values, formik) => {
    const initialInfo = removeBlankAliases(infoAbout(system));
    const changedInfo = flow(
      deepTrim,
      removeBlankAliases,
      removeUnchangedValues(initialInfo)
    )(values);

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
              You can change the name of the system and its description.
            </p>

            <Form>
              <TextField
                name="name"
                hint="What is the primary name for the system?"
                inputClassName="width-two-thirds"
                validate={(value) => {
                  return validateName(value, onBeforeNameChange, emptyIfUndefined(system.name))
                }}
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

              {withAliases && <AliasInputList />}

              <div className="update-info-form-controls">
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
