import React from "react";
import BaseLink from "../BaseLink"

import { NavLink } from 'react-router-dom';

//Filters out props which will be invalid once injected into GdsLink, thus removing console warnings
function InnerLink({ muted, textColour, noVisitedState, ...rest}) {
  return <NavLink {...rest} />;
}

function Link(props) {
  return <BaseLink element={InnerLink} {...props} />;
}

export default Link;
