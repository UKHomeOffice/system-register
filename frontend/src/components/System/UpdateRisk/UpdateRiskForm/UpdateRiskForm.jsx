import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { defaultTo, isEqual, mapValues, negate, some, trim } from "lodash-es";

import ErrorSummary from "../../../ErrorSummary/ErrorSummary";
import { FormikAwarePageTitle } from "../../../PageTitle";
import RadioGroup, { makeRadio } from "../../../RadioGroup";
import SecondaryButton from "../../../SecondaryButton";
import Textarea from "../../../Textarea";
import ValidationError from "../../../../services/validationError";
import toLower from "../../../../utilities/toLower";
import toSentenceCase from "../../../../utilities/toSentenceCase";
import { validateRationale } from "./validators";

import "./UpdateRiskForm.css";

const detailsOf = (risk) => ({
  level: defaultTo(risk.level, "unknown"),
  rationale: defaultTo(risk.rationale, ""),
});
const changesPresent = (initialValues, currentValues) =>
  some(currentValues, areDifferentFrom(initialValues));
const areDifferentFrom = (initialValues) => (value, key) =>
  isNotEqual(value, initialValues[key]);
const isNotEqual = negate(isEqual);
const trimSpaces = (values) => mapValues(values, trim);

const riskRatings = [
  makeRadio("low", "Low"),
  makeRadio("medium", "Medium"),
  makeRadio("high", "High"),
  makeRadio("unknown", "Unknown"),
  makeRadio("not_applicable", "Not applicable"),
];

function UpdateRiskForm({ risk, systemName, onSubmit, onCancel }) {
  const handleSubmit = useCallback(
    async (values, formik) => {
      const initialValues = detailsOf(risk);
      const trimmedValues = trimSpaces(values);
      const updateValues = changesPresent(initialValues, trimmedValues)
        ? { ...trimmedValues, lens: risk.name }
        : {};

      try {
        await onSubmit(updateValues);
      } catch (e) {
        if (e instanceof ValidationError) {
          formik.setErrors(e.errors);
        }
      }
    },
    [risk, onSubmit]
  );

  return (
    <Formik initialValues={detailsOf(risk)} onSubmit={handleSubmit}>
      <>
        <FormikAwarePageTitle>{`Update ${toLower(
          risk.name
        )} risk information — ${systemName}`}</FormikAwarePageTitle>

        <ErrorSummary order={["level", "rationale"]} />

        <Form>
          <h1>{systemName}</h1>
          <p className="update-risk-form__secondary">
            Please provide a high level assessment and enter a rationale for{" "}
            {toLower(risk.name)} risks associated with your system.
          </p>

          <RadioGroup
            name="level"
            items={riskRatings}
            hint="What is the level of risk?"
          >
            {toSentenceCase(risk.name)} risk rating
          </RadioGroup>

          <Textarea
            name="rationale"
            hint="Please provide a high-level overview to explain the selected risk rating."
            validate={validateRationale}
          >
            {toSentenceCase(risk.name)} rationale
          </Textarea>

          <div className="update-risk-form__risk-controls">
            <Button type="submit">Save</Button>
            <SecondaryButton onClick={onCancel}>Cancel</SecondaryButton>
          </div>
        </Form>
      </>
    </Formik>
  );
}

UpdateRiskForm.propTypes = {
  risk: PropTypes.shape({
    name: PropTypes.string.isRequired,
    level: PropTypes.string,
    rationale: PropTypes.string,
  }).isRequired,
  systemName: PropTypes.string.isRequired,
  onCancel: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
};

export default UpdateRiskForm;
