import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { isEqual, isObject, mapValues, negate, some, trim } from "lodash-es";
import { DateTime } from "luxon";

import DateField from "../../DateField/DateField";
import ErrorSummary from "../../ErrorSummary";
import PageTitle, { FormikAwarePageTitle } from "../../PageTitle";
import SecondaryButton from "../../SecondaryButton";
import Textarea from "../../Textarea";
import ValidationError from "../../../services/validationError";
import { validateAdditionalInformation } from "./validators";

import "./UpdateKeyDates.css";

const emptyIfUndefined = (value) => (value != null ? value : "");
const extractDateFields = (dateString) => {
  if (!dateString) {
    return { day: "", month: "", year: "" };
  }

  const date = new Date(dateString);
  return {
    day: String(date.getDate()),
    month: String(date.getMonth() + 1),
    year: String(date.getFullYear()),
  };
};

const keyDatesOf = (system) => {
  return {
    sunsetAdditionalInformation: emptyIfUndefined(
      system.sunset.additional_information
    ),
    sunsetDate: extractDateFields(system.sunset.date),
  };
};
const changesPresent = (initialValues, currentValues) =>
  some(currentValues, areDifferentFrom(initialValues));
const areDifferentFrom = (initialValues) => (value, key) =>
  isNotEqual(value, initialValues[key]);
const isNotEqual = negate(isEqual);
const deepTrim = (values) =>
  mapValues(values, (value) =>
    isObject(value) ? mapValues(value, trim) : trim(value)
  );

function UpdateKeyDates({ system, onCancel, onSubmit }) {
  const handleSubmit = useCallback(
    async (values, formik) => {
      const initialValues = keyDatesOf(system);
      const trimmedValues = deepTrim(values);
      const updatedValues = changesPresent(initialValues, trimmedValues)
        ? {
            sunset: {
              date: DateTime.local(
                +trimmedValues.sunsetDate.year,
                +trimmedValues.sunsetDate.month,
                +trimmedValues.sunsetDate.day
              ),
              additionalInformation: trimmedValues.sunsetAdditionalInformation,
            },
          }
        : {};

      try {
        await onSubmit(updatedValues);
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
          initialValues={keyDatesOf(system)}
          validateOnChange={false}
          onSubmit={handleSubmit}
        >
          <Form>
            <FormikAwarePageTitle>{`Update key dates — ${system.name}`}</FormikAwarePageTitle>

            <ErrorSummary
              order={["sunsetDate", "sunsetAdditionalInformation"]}
            />

            <h1>{system.name}</h1>
            <p className="update-key-dates-secondary">
              Please enter a sunset date and any additional information for the
              sunset date. A system is in sunset when it is in production but
              planned for end of life.
            </p>

            <DateField
              hintText="Please provide the date when the system is due for sunset, or leave all fields blank if not applicable. For example, 25 03 2021."
              name="sunsetDate"
            >
              Sunset date
            </DateField>

            <Textarea
              name="sunsetAdditionalInformation"
              hint="Please provide any relevant additional information for the sunset date, if applicable."
              inputClassName="width-two-thirds"
              validate={validateAdditionalInformation}
            >
              Additional information
            </Textarea>

            <div className="update-key-dates__form-controls">
              <Button type="submit">Save</Button>
              <SecondaryButton onClick={handleCancel}>Cancel</SecondaryButton>
            </div>
          </Form>
        </Formik>
      ) : (
        <>
          <PageTitle>Loading system...</PageTitle>

          <p>Loading system data...</p>
        </>
      )}
    </div>
  );
}

UpdateKeyDates.propTypes = {
  system: PropTypes.shape({
    name: PropTypes.string.isRequired,
    sunset: PropTypes.shape({
      date: PropTypes.string,
      additionalInformation: PropTypes.string,
    }).isRequired,
  }),
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
};

export default UpdateKeyDates;
