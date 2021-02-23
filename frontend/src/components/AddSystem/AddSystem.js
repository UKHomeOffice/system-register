import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { Route, Switch, useHistory, useRouteMatch } from "react-router-dom";

import AddSystemForm from "./AddSystemForm";
import AddSystemSuccess from "./AddSystemSuccess";
import api from "../../services/api";

import "./AddSystem.css";

export default function AddSystem({ onAdd, validateNewSystem }) {
  const history = useHistory();
  const { path } = useRouteMatch();

  const handleSubmit = useCallback(
    async (data) => {
      const { id, name } = await api.addSystem(data);
      onAdd();
      history.push(`${path}/success`, {
        id,
        name,
      });
    },
    [history, path, onAdd]
  );

  const handleCancel = useCallback(() => {
    history.push("/");
  }, [history]);

  return (
    <Switch>
      <Route path={path} exact>
        <AddSystemForm
          onSubmit={handleSubmit}
          onCancel={handleCancel}
          validate={validateNewSystem}
        />
      </Route>
      <Route path={`${path}/success`}>
        <AddSystemSuccess returnPath={path} />
      </Route>
    </Switch>
  );
}

AddSystem.propTypes = {
  onAdd: PropTypes.func.isRequired,
  validateNewSystem: PropTypes.func.isRequired,
};
