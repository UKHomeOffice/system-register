import React from "react";

import getDisplayName from "../../utilities/getDisplayName";
import { BlurOnNavigate } from "./index";

function withBlurOnNavigate(Component) {
  const WrappedComponent = (props) => {
    const ref = React.createRef();
    return (
      <div ref={ref} tabIndex={-1}>
        <BlurOnNavigate container={ref} />
        <Component {...props} />
      </div>
    );
  };
  WrappedComponent.displayName = `WithBlurOnNavigate(${getDisplayName(
    Component
  )})`;
  return WrappedComponent;
}

export default withBlurOnNavigate;
