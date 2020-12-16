import React from "react";
import { Field } from "formik";
import { Radio as GdsRadio } from "govuk-react";

function Radio({ children, name, value }) {
  return <Field name={name}>
    {({ field: { value: formikValue, ...fieldProps } }) => {
      const inputProps = {
        value,
        checked: value === formikValue,
        ...fieldProps
      };

      return (
        <GdsRadio name={name} {...inputProps}>
          {children}
        </GdsRadio>
      );
    }}
  </Field>;
}

export default Radio;
