import React from "react";
import { Field } from "formik";
import { InputField } from "govuk-react";

const undefinedIfNull = (value) => value !== null ? value : undefined;

function TextField({ children, hint, name, inputClassName }) {
  return <Field name={name}>
    {({ field: { value, ...fieldProps } }) => {
      const inputProps = {
        value: undefinedIfNull(value),
        className: inputClassName,
        ...fieldProps
      };

      return (
        <InputField hint={hint} input={inputProps}>
          {children}
        </InputField>
      );
    }}
  </Field>;
}

export default TextField;
