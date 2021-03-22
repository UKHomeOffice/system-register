import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import PageTitle, { FormikAwarePageTitle } from "../../PageTitle";
import PropTypes from "prop-types";

import "./UpdateKeyDates.css";
import Textarea from "../../Textarea";
import { Button } from "govuk-react";
import SecondaryButton from "../../SecondaryButton";
import DateField from "../../DateField/DateField";
import ValidationError from "../../../services/validationError";

const emptyIfUndefined = (value) => (value != null ? value : "");

const keyDateOf = (system) => ({
  sunset_additional_information: emptyIfUndefined(
    system.sunset.additional_information
  ),
  sunset_date: {
    day: emptyIfUndefined(String(new Date(system.sunset.date).getDate())),
    month: emptyIfUndefined(
      String(new Date(system.sunset.date).getMonth() + 1)
    ), //js getMonth() is zero-indexed
    year: emptyIfUndefined(String(new Date(system.sunset.date).getFullYear())),
  },
});

function UpdateKeyDates({ system, onCancel, onSubmit }) {
  const handleSubmit = useCallback(
    async (values, formik) => {
      const initialValues = keyDateOf(system);
      console.log(initialValues);
      console.log(values);
      try {
        await onSubmit(values);
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
          initialValues={keyDateOf(system)}
          validateOnChange={false}
          onSubmit={handleSubmit}
        >
          <Form>
            <FormikAwarePageTitle>{`Update key dates — ${system.name}`}</FormikAwarePageTitle>

            <h1>{system.name}</h1>
            <p className="update-key-dates-secondary">
              Please enter a sunset date and any additional information for the
              sunset date. A system is in sunset when it is in production but
              planned for end of life.
            </p>

            <DateField
              hintText="Please provide the date when the system is due for sunset. For example, 25 03 2021."
              name="sunset_date"
            >
              Sunset date
            </DateField>

            <Textarea
              name="sunset_additional_information"
              hint="Please provide any relevant additional information for the sunset date, if applicable."
              inputClassName="width-two-thirds"
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
  }),
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
};

export default UpdateKeyDates;
