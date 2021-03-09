import React from "react";
import PropTypes from "prop-types";

import KeyInfo from "../KeyInfo/KeyInfo";
import RiskBadge from "../RiskBadge/RiskBadge";
import toTitle from "../../../utilities/toTitle";

const NOT_APPLICABLE = "not_applicable";

function renderRationale(level, rationale, name) {
  if (level !== NOT_APPLICABLE) {
    return (
      <p data-testid={`risk-rationale-${name}`}>
        Rationale: <KeyInfo info={rationale} />
      </p>
    );
  }
}

function RiskDetails({ risk }) {
  return (
    <div data-testid="risk-details" className="riskSummary">
      <h3>{toTitle(risk.name)}</h3>
      <RiskBadge level={risk.level} />
      {renderRationale(risk.level, risk.rationale, risk.name)}
    </div>
  );
}

RiskDetails.propTypes = {
  risk: PropTypes.shape({
    name: PropTypes.string.isRequired,
    level: PropTypes.string,
    rationale: PropTypes.string,
  }).isRequired,
};

export default RiskDetails;
