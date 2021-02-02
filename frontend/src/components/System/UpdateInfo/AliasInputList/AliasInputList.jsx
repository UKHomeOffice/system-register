import React from "react";
import { Field, FieldArray, useFormikContext } from "formik";
import { Button, ErrorText, HintText, Input, LabelText } from "govuk-react";

import { validateAlias } from "./validators";

import "./AliasInputList.css";

const AliasInput = ({ name }) => (
  <Field name={name} validate={validateAlias}>
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
  const { values } = useFormikContext();

  return (
    <FieldArray name="aliases">
      {({ push, remove }) => (
        <div className="alias-input-list">
          <LabelText className="alias-input-list-title">Aliases</LabelText>
          <HintText>What is the system also known as?</HintText>

          {values.aliases && values.aliases.map(
            (alias, index) => (
              <div key={`field-${index}`}>
                <AliasInput name={`aliases[${index}]`}/>
                <Button className="alias-input-list-remove" type="button" onClick={() => {
                  remove(index);
                }}>Remove</Button>
              </div>
            )
          )}
          <Button className="alias-input-list-add" type="button" onClick={() => {
            push("");
          }}>Add another alias</Button>
        </div>
      )}
    </FieldArray>
  );
}

export default AliasInputList;
