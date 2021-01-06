import React from "react";
import GdsLink from '@govuk-react/link';

import "./BaseLink.css";

function BaseLink({ className, href, children, ...rest }) {
  const cls = `${className} system-register-link`;
  return <GdsLink href={href} className={cls} {...rest}>{children}</GdsLink>
}

export default BaseLink;
