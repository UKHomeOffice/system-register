import React from "react";
import AddSystemForm from "./AddSystemForm";

import "./AddSystem.css";
import PropTypes from "prop-types";

export default function AddSystem({ onBeforeNameChange }) {
  const handleAddSystem = (values) => {
    console.log(values);
    return values;
  };

  return (
    <AddSystemForm
      onSubmit={handleAddSystem}
      onBeforeNameChange={onBeforeNameChange}
    />
  );
}

AddSystem.propTypes = {
  onSubmit: PropTypes.func.isRequired,
  onBeforeNameChange: PropTypes.func.isRequired,
};
