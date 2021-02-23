import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { useHistory, useLocation } from "react-router-dom";

import Link from "../Linking/Link";

function AddSystemSuccess({ returnPath }) {
  const history = useHistory();
  const { state } = useLocation();

  useEffect(() => {
    if (state == null) {
      history.replace(returnPath);
    }
  }, [state, history, returnPath]);

  return state ? (
    <>
      <h1>System added to the Register</h1>
      <p>
        You can continue to{" "}
        <Link to={`/system/${state?.id}`}>
          complete key information for {state?.name}
        </Link>
        .
      </p>
    </>
  ) : null;
}

AddSystemSuccess.propTypes = {
  returnPath: PropTypes.string.isRequired,
};

export default AddSystemSuccess;
