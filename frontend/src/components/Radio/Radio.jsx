import React from "react";
import { Field } from "formik";
import { Radio as GdsRadio } from "govuk-react";

function Radio({ children, name, value, hint }) {
  return <Field name={name}>
    {({ field: { value: formikValue, ...fieldProps } }) => {
      const inputProps = {
        value,
        checked: value === formikValue,
        ...fieldProps
      };

      return (
        <GdsRadio name={name} hint={hint} {...inputProps}>
          {children}
        </GdsRadio>
      );
    }}
  </Field>;
}

export default Radio;
