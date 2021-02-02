import React from "react";
import { Field, FieldArray, useFormikContext } from "formik";
import { Button } from "govuk-react";

function AliasInputList() {
  const { values } = useFormikContext();

  return (
    <FieldArray name="aliases">
      {({ push, remove }) => (
        <>
          {values.aliases && values.aliases.map(
            (alias, index) => (
              <div key={`field-${index}`}>
                <Field name={`aliases[${index}]`}/>
                <Button onClick={() => {
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
