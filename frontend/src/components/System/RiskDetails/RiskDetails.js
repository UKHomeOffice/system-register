import React from "react";
import PropTypes from "prop-types";
import { useRouteMatch } from "react-router-dom";

import KeyInfo from "../KeyInfo/KeyInfo";
import Link from "../../Linking/Link";
import RiskBadge from "../RiskBadge/RiskBadge";
import toLower from "../../../utilities/toLower";
import toTitle from "../../../utilities/toTitle";

import "./RiskDetails.css";

const NOT_APPLICABLE = "not_applicable";

function renderRationale(level, rationale, name) {
  if (level !== NOT_APPLICABLE) {
    return (
      <p
        data-testid={`risk-rationale-${name}`}
        className="risk-details__rationale"
      >
        Rationale: <KeyInfo info={rationale} />
      </p>
    );
  }
}

function useUpdatedLocation(lens) {
  const { url } = useRouteMatch();
  return `${url}/update-risk?lens=${lens}`;
}

function RiskDetails({ risk }) {
  const updateUri = useUpdatedLocation(risk.name);

  return (
    <div data-testid="risk-details" className="risk-details">
      <h3 className="risk-details__title">{toTitle(risk.name)}</h3>

      <Link
        className="risk-details__update"
        to={updateUri}
        title={`Update ${toLower(risk.name)} risk information`}
      >
        Update
      </Link>

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
