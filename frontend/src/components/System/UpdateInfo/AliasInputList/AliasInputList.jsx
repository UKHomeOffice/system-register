import React from "react";
import { Field, FieldArray, useFormikContext } from "formik";
import { Button, ErrorText, HintText, Input, LabelText } from "govuk-react";

import { validateAlias } from "./validators";

import "./AliasInputList.css";

const AliasInput = ({ name }) => (
  <Field name={name} validate={validateAlias}>
    {({ field, meta }) => {
      const hasError = meta.touched && meta.error;

      return (
        <span>
          {hasError && <ErrorText>
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
        <>
          <LabelText>Aliases</LabelText>
          <HintText>What is the system also known as?</HintText>

          {values.aliases && values.aliases.map(
            (alias, index) => (
              <div key={`field-${index}`}>
                <AliasInput name={`aliases[${index}]`}/>
                <Button type="button" onClick={() => {
                  remove(index);
                }}>Remove</Button>
              </div>
            )
          )}
          <Button type="button" onClick={() => {
            push("");
          }}>Add another alias</Button>
        </>
      )}
    </FieldArray>
  );
}

export default AliasInputList;
