import React from "react";
import AddSystemForm from "./AddSystemForm";

import "./AddSystem.css";

export default function AddSystem() {
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

      <AddSystemForm onSubmit={handleAddSystem} />
    </div>
  );
}
