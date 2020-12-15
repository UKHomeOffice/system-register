import React from "react";
import { Field } from "formik";
import { InputField } from "govuk-react";

const emptyIfUndefined = (value) => value != null ? value : "";

function TextField({ children, hint, name, inputClassName }) {
  return <Field name={name}>
    {({ field: { value, ...fieldProps } }) => {
      const inputProps = {
        value: emptyIfUndefined(value),
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
