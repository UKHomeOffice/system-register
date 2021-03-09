import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { find } from "lodash-es";

import PageTitle from "../../PageTitle";
import UpdateRiskForm from "./UpdateRiskForm";
import { useQueryParams } from "../../../hooks";

const isMatchingRisk = (lens) => ({ name }) => name === lens;
const findMatchingRisk = (system, lens) => {
  const risks = system?.risks || [];
  return find(risks, isMatchingRisk(lens));
};

function UpdateRisk({ system, onSubmit, onClose }) {
  const { lens } = useQueryParams();
  const risk = findMatchingRisk(system, lens);

  useEffect(() => {
    if (system && !risk) {
      onClose();
    }
  }, [system, risk, onClose]);

  return (
    <div className="centerContent">
      {system && risk ? (
        <UpdateRiskForm
          systemName={system.name}
          risk={risk}
          onSubmit={onSubmit}
          onCancel={onClose}
        />
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
  onClose: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
};

export default UpdateRisk;
