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
    <div className="centerContent">
      <h1>Add a system to the register</h1>
      {/*secondary class not defined in app.css - should it be?*/}
      <p className="add-system-secondary">
        Please enter the name for the new system.
      </p>
      <AddSystemForm
        onSubmit={handleAddSystem}
        onBeforeNameChange={onBeforeNameChange}
      />
    </div>
  );
}

AddSystem.propTypes = {
  onBeforeNameChange: PropTypes.func.isRequired,
};
