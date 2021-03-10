import { GridCol, GridRow } from "govuk-react";
import RiskBadge from "../../../RiskBadge/RiskBadge";
import React from "react";

import "./RiskLevelsKey.css";

import riskDescriptions from "./risk-levels-data.json";

function getRiskDescriptions() {
  return riskDescriptions;
}

export default function RiskLevelsKey() {
  return (
    <>
      {getRiskDescriptions().map(({ level, description }) => (
        <GridRow className="risk-description-grid-row" key={level}>
          <GridCol setWidth="7em">
            <RiskBadge level={level} />
          </GridCol>
          <GridCol>
            <p>{description}</p>
          </GridCol>
        </GridRow>
      ))}
    </>
  );
}
