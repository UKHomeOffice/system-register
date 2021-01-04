import ModifiedDetails from "../../ModifiedDetails/ModifiedDetails";
import KeyInfo from "../KeyInfo/KeyInfo";
import { GridCol, GridRow } from "govuk-react";
import Criticality from "../Criticality/Criticality";
import InvestmentState from "../InvestmentState/InvestmentState";
import RiskDetails from "../RiskDetails/RiskDetails";
import React from "react";
import { Link } from 'react-router-dom'

const SystemView = ({ system }) => {
  return (
    <div className="systemDetails centerContent">
      {system ?
        (<>
          <div className="contentBlock">
            <h1>{system.name}</h1>
            <p data-testid="system-last-modified">
              <ModifiedDetails date={system.last_updated.timestamp}
                author_name={system.last_updated.author_name} />
            </p>
            <h2>Description</h2>
            <p data-testid="system-description"><KeyInfo info={system.description} /></p>
          </div>
          <div className="contentBlock">
            <h2>About</h2>
            <Link data-testid="about-change-link" className="gds-link change-link" to={window.location.pathname + `/update-about`}>Update</Link>
            {/* TODO discuss with team if better way to do relative path with react-router-dom */}
            <GridRow>
              <GridCol data-testid="about-column1" setWidth="one-quarter">
                <p>Aliases</p>
                <p>Department</p>
                <p>Criticality assessment</p>
                <p>Investment state</p>
                <p>Developed by</p>
                <p>Supported by</p>
              </GridCol>
              <GridCol data-testid="about-column2" setWidth="one-half">
                <p>{renderAliases(system.aliases)}</p>
                <p><KeyInfo info={system.department} /></p>
                <p><Criticality level={system.criticality} /></p>
                <p><InvestmentState state={system.investment_state} /></p>
                <p><KeyInfo info={system.developed_by} /></p>
                <p><KeyInfo info={system.supported_by} /></p>
              </GridCol>
            </GridRow>
          </div>
          <div className="contentBlock">
            <h2>Contacts</h2>
            <Link data-testid="contacts-change-link" className="gds-link change-link" to={window.location.pathname + `/update-contacts`}>Update</Link>
            <GridRow>
              <GridCol data-testid="contacts-column1" setWidth="one-quarter">
                <p>System register owner</p>
                <p>Business owner</p>
                <p>Technical owner</p>
                <p>Service owner</p>
                <p>Product owner</p>
                <p>Information asset owner</p>
              </GridCol>
              <GridCol data-testid="contacts-column2" setWidth="one-half">
                <p><KeyInfo info={system.system_register_owner} /></p>
                <p><KeyInfo info={system.business_owner} /></p>
                <p><KeyInfo info={system.technical_owner} /></p>
                <p><KeyInfo info={system.service_owner} /></p>
                <p><KeyInfo info={system.product_owner} /></p>
                <p><KeyInfo info={system.information_asset_owner} /></p>
              </GridCol>
            </GridRow>
          </div>
          <h2>Risk</h2>
          <div data-testid="risk-container" className="riskContainer">
            {system.risks.map(risk => <RiskDetails key={risk.name} risk={risk} />)}
          </div>
        </>)
        :
        (<p>loading system data...</p>)
      }
    </div>
  )
};


function renderAliases(aliases) {
  if (aliases.length > 0)
    return aliases.join(', ');
  else
    return 'None'
}
export default SystemView
