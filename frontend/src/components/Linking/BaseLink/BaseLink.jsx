import React from "react";
import GdsLink from '@govuk-react/link';

import "./BaseLink.css";

function BaseLink({ className, href, to, element, children, ...rest }) {
  const cls = `${className || ""} system-register-link`;
  const blurMenu = () => {
    var e = document.activeElement;
    if (e && e.blur) {
      e.blur()
    }
  }

  return <GdsLink onClick={blurMenu} as={element} to={to} href={href} className={cls} {...rest}>{children}</GdsLink>
}

export default BaseLink;
