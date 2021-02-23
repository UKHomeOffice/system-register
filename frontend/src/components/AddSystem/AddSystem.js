import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { useHistory } from "react-router-dom";

import AddSystemForm from "./AddSystemForm";
import api from "../../services/api";

import "./AddSystem.css";

export default function AddSystem({ onAdd, validateNewSystem }) {
  const history = useHistory();

  const handleSubmit = useCallback(
    async (data) => {
      await api.addSystem(data);
      onAdd();
    },
    [onAdd]
  );

  const handleCancel = useCallback(() => {
    history.push("/");
  }, [history]);

  return (
    <AddSystemForm
      onSubmit={handleSubmit}
      onCancel={handleCancel}
      validate={validateNewSystem}
    />
  );
}

AddSystem.propTypes = {
  onAdd: PropTypes.func.isRequired,
  validateNewSystem: PropTypes.func.isRequired,
};
