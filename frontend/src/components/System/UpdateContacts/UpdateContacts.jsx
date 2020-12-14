import React from "react";

function UpdateContacts({ system }) {
  return (
    <div className="systemDetails centerContent">
      {system ? (
        <h1>{system.name}</h1>
      ) : (
        <p>Loading system data...</p>
      )}
    </div>
  );
}

export default UpdateContacts;
