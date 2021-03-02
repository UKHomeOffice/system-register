import React from "react";
import PropTypes from "prop-types";

import PageTitle from "../../../PageTitle";

function SystemPageTitle({ children, status }) {
  const prefix = status === "success" ? "Update saved:" : null;
  const title = prefix ? `${prefix} ${children}` : children;

  return <PageTitle>{title}</PageTitle>;
}

SystemPageTitle.propTypes = {
  children: PropTypes.string.isRequired,
  status: PropTypes.oneOf(["success"]),
};

export default SystemPageTitle;
