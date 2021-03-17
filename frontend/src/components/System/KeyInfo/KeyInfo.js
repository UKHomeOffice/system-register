import React from "react";
import "./KeyInfo.css";
import PropTypes from "prop-types";

const KeyInfo = ({ info }) => {
  if (!info || info.toLowerCase() === "unknown") {
    return <strong className="unknownKeyInfo">UNKNOWN</strong>;
  } else if (info.toLowerCase() === "none") {
    return <strong className="unknownKeyInfo">NONE</strong>;
  } else if (info.toLowerCase() === "n/a") {
    return <strong className="naKeyInfo">N/A</strong>;
  } else return info;
};

KeyInfo.propTypes = {
  info: PropTypes.string,
};

export default KeyInfo;
