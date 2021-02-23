import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { useHistory, useLocation } from "react-router-dom";
import Panel from "@govuk-react/panel";

import Link from "../Linking/Link";

import "./addSystemSuccess.css";

function AddSystemSuccess({ returnPath }) {
  const history = useHistory();
  const { state } = useLocation();

  useEffect(() => {
    if (state == null) {
      history.replace(returnPath);
    }
  }, [state, history, returnPath]);

  return state ? (
    <div className="centerContent">
      <Panel
        title="System added to the Register"
        className="add-system-success-panel"
      />
      <h1 className="add-system-success-heading">Next Steps:</h1>
      <p>
        You can continue to{" "}
        <Link to={`/system/${state?.id}`}>
          complete key information for {state?.name}
        </Link>
        .
      </p>
    </div>
  ) : null;
}

AddSystemSuccess.propTypes = {
  returnPath: PropTypes.string.isRequired,
};

export default AddSystemSuccess;
