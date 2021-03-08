import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { find } from "lodash-es";
import { useHistory } from "react-router-dom";

import PageTitle from "../../PageTitle";
import UpdateRiskForm from "./UpdateRiskForm";
import { useQueryParams } from "../../../hooks";

const isMatchingRisk = (lens) => ({ name }) => name === lens;
const findMatchingRisk = (system, lens) => {
  const risks = system?.risks || [];
  return find(risks, isMatchingRisk(lens));
};

function UpdateRisk({ system, returnPath, onCancel }) {
  const history = useHistory();
  const { lens } = useQueryParams();
  const risk = findMatchingRisk(system, lens);

  useEffect(() => {
    if (system && !risk) {
      history.replace(returnPath);
    }
  }, [history, system, risk, returnPath]);

  return (
    <div className="centerContent">
      {system && risk ? (
        <>
          <PageTitle>{`Update ${lens} risk information — ${system.name}`}</PageTitle>

          <UpdateRiskForm
            systemName={system.name}
            risk={risk}
            onCancel={onCancel}
          />
        </>
      ) : (
        <>
          <PageTitle>Loading system...</PageTitle>

          <p>Loading system data...</p>
        </>
      )}
    </div>
  );
}

UpdateRisk.propTypes = {
  system: PropTypes.shape({
    name: PropTypes.string.isRequired,
    risks: PropTypes.arrayOf(
      PropTypes.shape({
        name: PropTypes.string.isRequired,
        rationale: PropTypes.string,
      })
    ).isRequired,
  }),
  returnPath: PropTypes.string.isRequired,
  onCancel: PropTypes.func.isRequired,
};

export default UpdateRisk;
