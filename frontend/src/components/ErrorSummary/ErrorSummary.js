import React from "react";
import GdsErrorSummary from "@govuk-react/error-summary";
import { useFormikContext } from "formik";
import { filter, flow, indexOf, map, sortBy } from "lodash-es";

const handleErrorClick = (targetName) => {
  document.getElementsByName(targetName)[0].scrollIntoView();
};

const mapToErrorObjects = (errors) => map(errors, (text, fieldName) => ({ targetName: fieldName, text }));
const filterOnlyFieldsThatWereTouched = (touched) => (errors) => filter(errors, ({ targetName }) => touched[targetName]);
const sortByFieldOrder = (order) => (errors) => sortBy(errors, ({ targetName }) => indexOf(order, targetName));

function ErrorSummary({ order }) {
  const { errors: formErrors, touched } = useFormikContext();
  const errors = flow(
    mapToErrorObjects,
    filterOnlyFieldsThatWereTouched(touched),
    sortByFieldOrder(order))(formErrors);

  return errors.length !== 0
    ? <GdsErrorSummary onHandleErrorClick={handleErrorClick} errors={errors} />
    : null;
}

export default ErrorSummary;