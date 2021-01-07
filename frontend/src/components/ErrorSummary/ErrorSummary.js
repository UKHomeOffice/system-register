import React from "react";
import GdsErrorSummary from "@govuk-react/error-summary";
import { useFormikContext } from "formik";
import { chain, indexOf } from "lodash-es";

const handleErrorClick = (targetName) => {
  document.getElementsByName(targetName)[0].scrollIntoView();
};

const toErrorObjects = (text, fieldName) => ({ targetName: fieldName, text });
const onlyFieldsThatWere = (touched) => ({ targetName }) => touched[targetName];
const field = (order) => ({ targetName }) => indexOf(order, targetName);

function ErrorSummary({ order }) {
  const { errors: formErrors, touched } = useFormikContext();
  const errors = chain(formErrors)
    .map(toErrorObjects)
    .filter(onlyFieldsThatWere(touched))
    .sortBy(field(order))
    .value();

  return errors.length !== 0
    ? <GdsErrorSummary onHandleErrorClick={handleErrorClick} errors={errors} />
    : null;
}

export default ErrorSummary;