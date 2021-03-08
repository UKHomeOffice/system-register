import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { defaultTo, isEqual, omitBy } from "lodash-es";

import RadioGroup, { makeRadio } from "../../../RadioGroup";
import SecondaryButton from "../../../SecondaryButton";
import Textarea from "../../../Textarea";
import toLower from "../../../../utilities/toLower";
import toTitle from "../../../../utilities/toTitle";

const detailsOf = (risk) => ({
  level: defaultTo(risk.level, "unknown"),
  rationale: defaultTo(risk.rationale, ""),
});
const removeUnchangedValues = (initialValues, currentValues) =>
  omitBy(currentValues, (value, key) => isEqual(value, initialValues[key]));

const riskRatings = [
  makeRadio("low", "Low"),
  makeRadio("medium", "Medium"),
  makeRadio("high", "High"),
  makeRadio("unknown", "Unknown"),
  makeRadio("not_applicable", "Not applicable"),
];

function UpdateRiskForm({ risk, systemName, onSubmit, onCancel }) {
  const handleSubmit = useCallback(
    async (values) => {
      const initialValues = detailsOf(risk);
      const changedValues = removeUnchangedValues(initialValues, values);
      await onSubmit(changedValues);
    },
    [risk, onSubmit]
  );

  return (
    <Formik initialValues={detailsOf(risk)} onSubmit={handleSubmit}>
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
          {toTitle(risk.name)} risk rating
        </RadioGroup>

        <Textarea
          name="rationale"
          hint="Please provide a high-level overview to explain the selected risk rating"
        >
          {toTitle(risk.name)} rationale
        </Textarea>

        <div className="update-risk-form__risk-controls">
          <Button type="submit">Save</Button>
          <SecondaryButton onClick={onCancel}>Cancel</SecondaryButton>
        </div>
      </Form>
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
