import React from "react";
import { Field, FieldArray, useFormikContext } from "formik";
import { ErrorText, HintText, Input, LabelText } from "govuk-react";
import { isString } from "lodash-es";

import SecondaryButton from "../../../SecondaryButton";

import "./AliasInputList.css";

const AliasInput = ({ name }) => (
  <Field name={name}>
    {({ field, meta }) => {
      const hasError = meta.touched && meta.error;
      const className = `alias-input-list-item ${hasError ? "alias-input-error" : ""}`

      return (
        <span className={className}>
          {hasError && <ErrorText>
            <span className="alias-input-hidden">Error:</span>
            {meta.error}
          </ErrorText>}

          <Input error={hasError} {...field} />
        </span>
      );
    }}
  </Field>
);

function AliasInputList() {
  const { values, errors } = useFormikContext();

  return (
    <FieldArray name="aliases">
      {({ push, remove }) => (
        <div className="alias-input-list">
          <LabelText className="alias-input-list-title">Aliases</LabelText>
          <HintText>What is the system also known as?</HintText>

          {isString(errors.aliases) && <ErrorText>{errors.aliases}</ErrorText>}

          {values.aliases && values.aliases.map(
            (alias, index) => (
              <div key={`field-${index}`} className="alias-input-list-row">
                <AliasInput name={`aliases[${index}]`}/>
                <SecondaryButton className="alias-input-list-remove" onClick={() => {
                  remove(index);
                }}>Remove</SecondaryButton>
              </div>
            )
          )}
          <SecondaryButton className="alias-input-list-add" onClick={() => {
            push("");
          }}>Add another alias</SecondaryButton>
        </div>
      )}
    </FieldArray>
  );
}

export default AliasInputList;
