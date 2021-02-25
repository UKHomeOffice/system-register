import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import {
  difference,
  flow,
  isArray,
  isEmpty,
  isEqual,
  map,
  mapValues,
  omitBy,
  reject,
  trim,
  update,
} from "lodash-es";

import AliasInputList from "../../AliasInputList";
import ErrorSummary from "../../ErrorSummary/ErrorSummary";
import Textarea from "../../Textarea";
import TextField from "../../TextField";
import ValidationError from "../../../services/validationError";
import {
  validateAliases,
  validateDescription,
  validateName,
} from "./validators";
import SecondaryButton from "../../SecondaryButton";

import "./UpdateInfo.css";
import PropTypes from "prop-types";

const emptyIfUndefined = (value) => (value != null ? value : "");
const emptyArrayIfUndefined = (value) => (value != null ? value : []);
const deepTrim = (values) =>
  mapValues(values, (value) =>
    isArray(value) ? map(value, trim) : trim(value)
  );
const removeBlankAliases = (values) =>
  update(values, "aliases", (aliases) => reject(aliases, isEmpty));
const removeUnchangedValues = (initialValues) => (values) =>
  omitBy(values, (value, key) => {
    return isArray(value)
      ? sizeAndContentAreTheSame(value, initialValues[key])
      : isEqual(value, initialValues[key]);
  });

function sizeAndContentAreTheSame(arr1, arr2) {
  if (arr1.length !== arr2.length) {
    return false;
  }
  return isEmpty(difference(arr1, arr2));
}

const infoAbout = (system) => ({
  name: emptyIfUndefined(system.name),
  description: emptyIfUndefined(system.description),
  aliases: [...emptyArrayIfUndefined(system.aliases), ""],
});

function UpdateInfo({ system, onSubmit, onCancel, onBeforeNameChange }) {
  const handleSubmit = useCallback(
    async (values, formik) => {
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
    },
    [system, onSubmit]
  );

  const handleCancel = () => {
    onCancel();
  };

  return (
    <div className="centerContent">
      {system ? (
        <Formik
          initialValues={infoAbout(system)}
          validateOnChange={false}
          validate={validateAliases}
          onSubmit={handleSubmit}
        >
          <>
            <ErrorSummary order={["name", "description", "aliases"]} />

            <h1>{system.name}</h1>
            <p className="update-info-secondary">
              You can change the name of the system, its description and
              aliases.
            </p>

            <Form>
              <TextField
                name="name"
                hint="What is the primary name for the system?"
                inputClassName="width-two-thirds"
                validate={(value) => {
                  return validateName(
                    value,
                    onBeforeNameChange,
                    emptyIfUndefined(system.name)
                  );
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

              <AliasInputList />

              <div className="update-info-form-controls">
                <Button type="submit">Save</Button>
                <SecondaryButton onClick={handleCancel}>Cancel</SecondaryButton>
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

UpdateInfo.propTypes = {
  system: PropTypes.shape({
    name: PropTypes.string.isRequired,
    aliases: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    description: PropTypes.string,
  }),
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  onBeforeNameChange: PropTypes.func.isRequired,
};

export default UpdateInfo;
