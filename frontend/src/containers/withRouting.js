import React from "react";
import { BrowserRouter } from "react-router-dom";

const withRouting = (Component) => {
  const RouteWrapped = (props) => {
    return (
      <BrowserRouter>
        <Component {...props} />
      </BrowserRouter>
    );
  };

  return RouteWrapped;
};

export default withRouting;
