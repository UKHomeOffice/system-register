import React from "react";
import KeyInfo from "../System/KeyInfo/KeyInfo";
import PropTypes from "prop-types";

const formatDate = (str) => {
  if (str) {
    const options = { year: "numeric", month: "long", day: "numeric" };
    return new Date(str).toLocaleDateString("en-GB", options);
  }
};

const positiveInvestmentStates = ["evergreen", "invest", "maintain"];
const endOfLifeInvestmentStates = ["decommissioned", "sunset"];

const SunsetDate = ({ investment_state, date }) => {
  if (date) {
    return <KeyInfo info={formatDate(date)} />;
  } else {
    if (investment_state === "unknown") {
      return <KeyInfo info={investment_state} />;
    } else if (positiveInvestmentStates.includes(investment_state)) {
      return <KeyInfo info="none" />;
    } else if (endOfLifeInvestmentStates.includes(investment_state)) {
      return <KeyInfo info="unknown" />;
    } else {
      return <KeyInfo info="N/A" />;
    }
  }
};

SunsetDate.propTypes = {
  investment_state: PropTypes.string.isRequired,
  date: PropTypes.string,
};

export default SunsetDate;
