import React from "react";
import PropTypes from "prop-types";
import { Field } from "formik";
import { DateField as GovDateField } from "govuk-react";

import "./DateField.css";

function DateField({ children, hintText, name }) {
  return (
    <Field name={name}>
      {({ field: { value }, form: { setFieldValue } }) => {
        return (
          <GovDateField
            hintText={hintText}
            input={{
              value,
              onChange: (e) => {
                setFieldValue(name, e);
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
};
