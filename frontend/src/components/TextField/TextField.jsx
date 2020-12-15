import React from "react";
import { Field } from "formik";
import { InputField } from "govuk-react";

const undefinedIfNull = (value) => value !== null ? value : undefined;

function TextField({ children, hint, name, inputClassName }) {
  return <Field name={name}>
    {({ field: { value, ...fieldProps } }) => (
      <InputField hint={hint}
                  input={{ value: undefinedIfNull(value), className: inputClassName, ...fieldProps }}>
        {children}
      </InputField>
    )}
  </Field>;
}

export default TextField;
