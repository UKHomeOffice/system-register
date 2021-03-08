import React from "react";
import PropTypes from "prop-types";
import { Form, Formik } from "formik";
import { defaultTo } from "lodash-es";

import RadioGroup, { makeRadio } from "../../../RadioGroup";
import SecondaryButton from "../../../SecondaryButton";
import Textarea from "../../../Textarea";
import toLower from "../../../../utilities/toLower";
import toTitle from "../../../../utilities/toTitle";

const detailsOf = (risk) => ({
  level: defaultTo(risk.level, "unknown"),
  rationale: defaultTo(risk.rationale, ""),
});

const riskRatings = [
  makeRadio("low", "Low"),
  makeRadio("medium", "Medium"),
  makeRadio("high", "High"),
  makeRadio("unknown", "Unknown"),
  makeRadio("not_applicable", "Not applicable"),
];

function UpdateRiskForm({ risk, systemName, onCancel }) {
  return (
    <Formik initialValues={detailsOf(risk)} onSubmit={() => {}}>
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
};

export default UpdateRiskForm;
