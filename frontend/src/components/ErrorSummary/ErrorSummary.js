import React, { useEffect } from "react";
import GdsErrorSummary from "@govuk-react/error-summary";
import { useFormikContext } from "formik";
import {
  filter,
  find,
  findIndex,
  flatMap,
  flow,
  get,
  includes,
  isArray,
  isEqual,
  isUndefined,
  lowerCase,
  map,
  reject,
  sortBy,
  startsWith
} from "lodash-es";

import usePrevious from "../../utilities/usePrevious";

const isFormControl = (element) => includes(["input", "textarea"], lowerCase(element.nodeName));

const handleErrorClick = (targetName) => {
  const field = find(document.getElementsByName(targetName), isFormControl);
  if (field) {
    field.scrollIntoView();
    field.focus();
  }
};

const newErrorObject = (field, text) => ({ targetName: field, text });
const mapAllToErrorObjects = (field, array) => map(array, (text, index) => newErrorObject(`${field}[${index}]`, text));
const mapToSingleErrorObject = (field, text) => [newErrorObject(field, text)];
const findPositionInOrder = (order, name) => findIndex(order, (key) => startsWith(name, key));
const wasTouched = (touched) => ({ targetName }) => get(touched, targetName);

const mapToErrorObjects = (errors) => flatMap(errors, (text, fieldName) => {
  return isArray(text)
    ? mapAllToErrorObjects(fieldName, text)
    : mapToSingleErrorObject(fieldName, text);
});
const rejectValidFields = (errors) => reject(errors, (error) => isUndefined(error.text));
const filterOnlyFieldsThatWereTouched = (touched) => (errors) => filter(errors, wasTouched(touched));
const sortByFieldOrder = (order) => (errors) => sortBy(errors, ({ targetName }) => findPositionInOrder(order, targetName));

function ErrorSummary({ order }) {
  const { errors: formErrors, touched } = useFormikContext();
  const errors = flow(
    mapToErrorObjects,
    rejectValidFields,
    filterOnlyFieldsThatWereTouched(touched),
    sortByFieldOrder(order)
  )(formErrors);
  const prevErrors = usePrevious(errors);

  useEffect(() => {
    if ((errors.length !== 0) && !isEqual(errors, prevErrors)) {
      document.getElementById("error-summary").focus();
    }
  }, [errors, prevErrors]);

  return errors.length !== 0
    ? <GdsErrorSummary id="error-summary" onHandleErrorClick={handleErrorClick} errors={errors}/>
    : null;
}

export default ErrorSummary;
