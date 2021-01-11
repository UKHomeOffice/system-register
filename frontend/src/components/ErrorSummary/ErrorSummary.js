import React, { useEffect } from "react";
import GdsErrorSummary from "@govuk-react/error-summary";
import { useFormikContext } from "formik";
import { filter, flow, indexOf, isEqual, map, sortBy } from "lodash-es";

import usePrevious from "../../utilities/usePrevious";

const handleErrorClick = (targetName) => {
  const field = document.getElementsByName(targetName)[0];
  if (field) {
    field.scrollIntoView();
    field.focus();
  }
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
  const prevErrors = usePrevious(errors);

  useEffect(() => {
    if ((errors.length !== 0) && !isEqual(errors, prevErrors)) {
      document.getElementById("error-summary").focus();
    }
  }, [errors, prevErrors]);

  return errors.length !== 0
    ? <GdsErrorSummary id="error-summary" onHandleErrorClick={handleErrorClick} errors={errors} />
    : null;
}

export default ErrorSummary;
