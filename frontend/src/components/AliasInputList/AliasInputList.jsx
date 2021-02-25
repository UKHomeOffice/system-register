import React, { useEffect, useState } from "react";
import { Field, FieldArray, useFormikContext } from "formik";
import { ErrorText, HintText, Input, LabelText } from "govuk-react";
import { isString, reject } from "lodash-es";

import SecondaryButton from "../SecondaryButton";

import "./AliasInputList.css";
import PropTypes from "prop-types";

const AliasInput = ({ name }) => (
  <Field name={name}>
    {({ field, meta }) => {
      const hasError = meta.touched && meta.error;
      const className = `alias-input-list-item ${
        hasError ? "alias-input-error" : ""
      }`;

      return (
        <span className={className}>
          {hasError && (
            <ErrorText>
              <span className="alias-input-hidden">Error:</span>
              {meta.error}
            </ErrorText>
          )}

          <Input error={hasError} {...field} />
        </span>
      );
    }}
  </Field>
);

function AliasInputList() {
  const { values, errors } = useFormikContext();
  const [focusedIndex, setFocusedIndex] = useState(undefined);

  useEffect(() => {
    if (focusedIndex !== undefined) {
      const fields = document.querySelectorAll(".alias-input-list-item input");
      if (focusedIndex < fields.length) {
        fields[focusedIndex].focus();
      }
      setFocusedIndex(undefined);
    }
  }, [focusedIndex]);

  return (
    <FieldArray name="aliases">
      {({ form, push, remove }) => (
        <div className="alias-input-list">
          <LabelText className="alias-input-list-title" name="aliasesLabel">
            Aliases
          </LabelText>
          <HintText>What is the system also known as?</HintText>

          {isString(errors.aliases) && <ErrorText>{errors.aliases}</ErrorText>}

          {values.aliases &&
            values.aliases.map((alias, index) => (
              <div key={`field-${index}`} className="alias-input-list-row">
                <AliasInput name={`aliases[${index}]`} />
                <SecondaryButton
                  className="alias-input-list-remove"
                  onClick={() => {
                    if (values.aliases.length === 1) {
                      document.querySelector(".alias-input-list-add").focus();
                    } else if (index === values.aliases.length - 1) {
                      setFocusedIndex(index - 1);
                    } else {
                      setFocusedIndex(index);
                    }
                    remove(index);
                    // noinspection JSIgnoredPromiseFromCall
                    form.validateForm({
                      ...form.values,
                      aliases: reject(values.aliases, (_, i) => i === index),
                    });
                  }}
                >
                  Remove
                </SecondaryButton>
              </div>
            ))}
          <SecondaryButton
            className="alias-input-list-add"
            onClick={() => {
              push("");
            }}
          >
            Add another alias
          </SecondaryButton>
        </div>
      )}
    </FieldArray>
  );
}

export default AliasInputList;

AliasInput.propTypes = {
  name: PropTypes.string.isRequired,
};
