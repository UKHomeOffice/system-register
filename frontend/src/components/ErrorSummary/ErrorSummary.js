import React from "react";
import {useFormikContext} from "formik";
import GdsErrorSummary from '@govuk-react/error-summary';
import {map} from "lodash-es";

const handleErrorClick = (targetName) => {
  document.getElementsByName(targetName)[0].scrollIntoView();
};

function ErrorSummary() {
  const {errors: formErrors} = useFormikContext();
  const errors = map(formErrors, (text, fieldName) => ({targetName: fieldName, text}));

  return  errors.length === 0 ? null : <GdsErrorSummary onHandleErrorClick={handleErrorClick} errors={errors}/>;
}

export default ErrorSummary;