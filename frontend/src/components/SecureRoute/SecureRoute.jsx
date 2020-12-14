import React from "react";
// noinspection ES6CheckImport
import { Route } from "react-router-dom";
import { useKeycloak } from "@react-keycloak/web";

function SecureRoute({ component: Component, children, render, ...rest }) {
  const { initialized, keycloak } = useKeycloak();

  return <Route {...rest} render={(props) => {
    if (!initialized || !keycloak) {
      return <p>Loading authentication settings...</p>;
    } else if (!keycloak.authenticated) {
      keycloak.login();
      return <p>Redirecting to Keycloak...</p>;
    }
    return children
      ? children
      : Component
        ? <Component {...props} />
        : render(props);
  }} />;
}

export default SecureRoute;
