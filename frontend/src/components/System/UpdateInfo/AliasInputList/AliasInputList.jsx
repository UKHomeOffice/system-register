import React from "react";
import { Field, FieldArray, useFormikContext } from "formik";
import { Button } from "govuk-react";

function AliasInputList() {
  const { values } = useFormikContext();

  return (
    <FieldArray name="aliases">
      {({ remove }) => values.aliases && values.aliases.map(
        (alias, index) => (
          <div key={`field-${index}`}>
            <Field name={`aliases[${index}]`}/>
            <Button onClick={() => {
              remove(index);
            }}>Remove</Button>
          </div>
        )
      )}
    </FieldArray>
  );
}

export default AliasInputList;
