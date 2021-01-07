import React from "react";
import { Field } from "formik";
import { TextArea } from "govuk-react";

import "./Textarea.css";

const emptyIfUndefined = (value) => value != null ? value : "";

function Textarea({ children, hint, name, placeholder, validate, inputClassName }) {
  return <Field name={name} validate={validate}>
    {({ field: { value, ...fieldProps }, meta: { error: errorText, touched } }) => {
      const inputProps = {
        value: emptyIfUndefined(value),
        className: inputClassName,
        placeholder,
        ...fieldProps
      };
      const error = errorText && touched && (
        <>
          <span className="visually-hidden">Error:</span>
          {errorText}
        </>
      );

      return (
        <span className="text-area">
          <TextArea hint={hint} input={inputProps} meta={{ error, touched }}>
            {children}
          </TextArea>
        </span>
      );
    }}
  </Field>;
}

export default Textarea;
