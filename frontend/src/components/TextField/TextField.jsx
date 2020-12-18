import React from "react";
import { Field } from "formik";
import { InputField } from "govuk-react";

import "./TextField.css";

const emptyIfUndefined = (value) => value != null ? value : "";

function TextField({ children, hint, name, placeholder, validate, inputClassName }) {
  return <Field name={name} validate={validate}>
    {({ field: { value, ...fieldProps }, meta: { error: errorText, touched } }) => {
      const inputProps = {
        value: emptyIfUndefined(value),
        className: inputClassName,
        placeholder,
        ...fieldProps
      };
      const error = errorText && (
        <>
          <span className="visually-hidden">Error:</span>
          {errorText}
        </>
      );

      return (
        <span className="text-field">
          <InputField hint={hint} input={inputProps} meta={{ error, touched }}>
            {children}
          </InputField>
        </span>
      );
    }}
  </Field>;
}

export default TextField;
