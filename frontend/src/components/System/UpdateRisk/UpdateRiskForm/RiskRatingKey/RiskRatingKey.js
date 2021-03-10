import { GridCol, GridRow } from "govuk-react";
import RiskBadge from "../../../RiskBadge/RiskBadge";
import React from "react";

import "./RiskRatingKey.css";

import riskDescriptions from "./risk-ratings-data.json";

function getRiskDescriptions() {
  return riskDescriptions;
}

export default function RiskRatingKey() {
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
