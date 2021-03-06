import React from "react";
import PropTypes from "prop-types";
import { Field } from "formik";
import { Radio as GdsRadio } from "govuk-react";

function Radio({ children, name, value, hint, title }) {
  return (
    <Field name={name}>
      {({ field: { value: formikValue, ...fieldProps } }) => {
        const inputProps = {
          value,
          title,
          checked: value === formikValue,
          ...fieldProps,
        };

        return (
          <GdsRadio name={name} hint={hint} {...inputProps}>
            {children}
          </GdsRadio>
        );
      }}
    </Field>
  );
}

Radio.propTypes = {
  children: PropTypes.node.isRequired,
  name: PropTypes.string,
  value: PropTypes.string,
  hint: PropTypes.node,
  title: PropTypes.string,
};

export default Radio;
