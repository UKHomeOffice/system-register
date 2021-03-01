import React from "react";
import PropTypes from "prop-types";
import { useFormikContext } from "formik";
import { isEmpty } from "lodash-es";

import PageTitle from ".";

function FormikAwarePageTitle({ children }) {
  const { errors } = useFormikContext();

  const text = isEmpty(errors) ? children : `Error: ${children}`;

  return <PageTitle>{text}</PageTitle>;
}

FormikAwarePageTitle.propTypes = {
  children: PropTypes.string.isRequired,
};

export default FormikAwarePageTitle;
