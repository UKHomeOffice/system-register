import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { Field } from "formik";
import { DateField as GovDateField } from "govuk-react";

import { validateDate } from "./validators";

import "./DateField.css";

function DateField({ children, hintText, name, validate: customValidator }) {
  const validate = useCallback(
    (values) => {
      const isInvalidDate = validateDate(values);
      if (isInvalidDate) {
        return isInvalidDate;
      }
      if (customValidator) {
        return customValidator(values);
      }
    },
    [customValidator]
  );

  return (
    <Field name={name} validate={validate}>
      {({
        field: { value },
        form: { setFieldValue, setFieldTouched },
        meta: { error },
      }) => {
        return (
          <GovDateField
            className="date-field"
            hintText={hintText}
            errorText={error}
            input={{
              value,
              onChange: (values) => {
                setFieldValue(name, values);
              },
              onBlur: () => {
                setFieldTouched(name);
              },
            }}
          >
            {children}
          </GovDateField>
        );
      }}
    </Field>
  );
}

export default DateField;

DateField.propTypes = {
  children: PropTypes.string,
  hintText: PropTypes.string,
  name: PropTypes.string,
  validate: PropTypes.func,
};
