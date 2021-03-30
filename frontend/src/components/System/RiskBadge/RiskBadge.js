import React from "react";
import "./RiskBadge.css";
import toUpper from "../../../utilities/toUpper";
import PropTypes from "prop-types";

const NOT_APPLICABLE = "not_applicable";

const RiskBadge = ({ level }) => {
  let riskClass;
  switch (level) {
    case "high":
      riskClass = "badge-highRisk";
      break;
    case "medium":
      riskClass = "badge-mediumRisk";
      break;
    case "low":
      riskClass = "badge-lowRisk";
      break;
    case NOT_APPLICABLE:
      riskClass = "badge-noRisk";
      break;
    default:
      riskClass = "badge-unknownRisk";
  }

  return <span className={`badge ${riskClass}`}>{formatLevel(level)}</span>;
};

function formatLevel(level) {
  if (!level) return "UNKNOWN";
  if (level === NOT_APPLICABLE) {
    return "N/A";
  }
  return toUpper(level);
}

RiskBadge.propTypes = {
  level: PropTypes.oneOf(["high", "medium", "low", NOT_APPLICABLE, "unknown"]),
};

export default RiskBadge;
