import React from "react";
import PropTypes from "prop-types";
import { Field } from "formik";
import { DateField as GovDateField } from "govuk-react";

import "./DateField.css";

function DateField({ children, hintText, name, validate }) {
  return (
    <Field name={name} validate={validate}>
      {({
        field: { value },
        form: { setFieldValue, setFieldTouched },
        meta: { error },
      }) => {
        return (
          <GovDateField
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
