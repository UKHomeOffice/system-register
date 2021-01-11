import React from "react";
import './../CommonLink.css'

import { Link as ReactRouterLink } from 'react-router-dom';

function Link({ className, to, children, ...rest }) {
  const cls = `${className} system-register-link`;
  return <ReactRouterLink to={to} className={cls} {...rest}>{children}</ReactRouterLink>
}

export default Link;
