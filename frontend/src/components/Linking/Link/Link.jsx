import React from "react";
import BaseLink from "../BaseLink"

import { NavLink } from 'react-router-dom';

function Link(props) {
  return <BaseLink element={NavLink} {...props} />;
}

export default Link;
