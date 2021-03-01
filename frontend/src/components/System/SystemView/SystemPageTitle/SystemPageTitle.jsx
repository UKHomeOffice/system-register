import React from "react";
import PropTypes from "prop-types";

import PageTitle from "../../../PageTitle";

function SystemPageTitle({ name: systemName, status }) {
  const name = systemName || "Loading system...";
  const prefix = status === "success" ? "Update saved:" : null;
  const title = prefix ? `${prefix} ${name}` : name;

  return <PageTitle>{title}</PageTitle>;
}

SystemPageTitle.propTypes = {
  name: PropTypes.string,
  status: PropTypes.oneOf(["success"]),
};

export default SystemPageTitle;
