import React, { useRef, useEffect } from "react";
import GdsLink from '@govuk-react/link';

import "./BaseLink.css";

function BaseLink({ className, href, to, element, children, ...rest }) {
  const cls = `${className} system-register-link`;
  return <GdsLink as={element} to={to} href={href} className={cls} {...rest}>{children}</GdsLink>
}

export default BaseLink;
