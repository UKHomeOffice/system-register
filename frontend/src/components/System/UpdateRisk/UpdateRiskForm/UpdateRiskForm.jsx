import React from "react";
import PropTypes from "prop-types";

import SecondaryButton from "../../../SecondaryButton";
import toLower from "../../../../utilities/toLower";

function UpdateRiskForm({ risk, systemName, onCancel }) {
  return (
    <>
      <h1>{systemName}</h1>
      <p className="update-risk-form__secondary">
        Please provide a high level assessment and enter a rationale for{" "}
        {toLower(risk.name)} risks associated with your system.
      </p>

      <div className="update-risk-form--risk-controls">
        <SecondaryButton onClick={onCancel}>Cancel</SecondaryButton>
      </div>
    </>
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
