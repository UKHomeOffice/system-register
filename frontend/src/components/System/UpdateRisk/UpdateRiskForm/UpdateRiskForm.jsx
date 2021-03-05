import React from "react";
import PropTypes from "prop-types";
import { Form, Formik } from "formik";
import { defaultTo, map } from "lodash-es";

import Radio from "../../../Radio";
import SecondaryButton from "../../../SecondaryButton";
import toLower from "../../../../utilities/toLower";

const detailsOf = (risk) => ({
  level: defaultTo(risk.level, "unknown"),
});
const makeRating = (value, text) => ({ value, text });
const toRadioButton = ({ value, text }, index) => (
  <Radio key={index} name="level" value={value}>
    {text}
  </Radio>
);

const riskRatings = [
  makeRating("low", "Low"),
  makeRating("medium", "Medium"),
  makeRating("high", "High"),
  makeRating("unknown", "Unknown"),
  makeRating("not_applicable", "Not applicable"),
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

        <h2 className="update-risk-form__group-title">Roadmap risk rating</h2>
        <p className="update-risk-form__secondary">
          What is the level of risk?
        </p>
        {map(riskRatings, toRadioButton)}

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
    rationale: PropTypes.string.isRequired,
  }).isRequired,
  systemName: PropTypes.string.isRequired,
  onCancel: PropTypes.func.isRequired,
};

export default UpdateRiskForm;
