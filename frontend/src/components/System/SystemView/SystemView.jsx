import React from "react";
import PropTypes from "prop-types";
import { GridCol, GridRow } from "govuk-react";

import Criticality from "../Criticality/Criticality";
import InvestmentState from "../InvestmentState/InvestmentState";
import KeyInfo from "../KeyInfo/KeyInfo";
import Link from "../../Linking/Link";
import ModifiedDetails from "../../ModifiedDetails/ModifiedDetails";
import RiskDetails from "../RiskDetails/RiskDetails";
import SystemPageTitle from "./SystemPageTitle";
import UpdateSuccessMessage from "../UpdateSuccessMessage";
import useOnUnmount from "../../../utilities/useOnUnmount";

import "./SystemView.css";

function buildStatusNotification(status) {
  if (status === "success") {
    return <UpdateSuccessMessage />;
  }
  return null;
}

function SystemView({ system, status, onClose }) {
  useOnUnmount(onClose);

  return (
    <div className="systemDetails centerContent">
      <SystemPageTitle name={system?.name} status={status} />

      {system ? (
        <>
          <div className="contentBlock">
            {buildStatusNotification(status)}

            <h1>{system.name}</h1>
            <Link
              data-testid="info-change-link"
              className="change-link"
              to={window.location.pathname + `/update-info`}
            >
              Update
            </Link>
            <p data-testid="system-last-modified">
              <ModifiedDetails
                date={system.last_updated.timestamp}
                author_name={system.last_updated.author_name}
              />
            </p>
            <h2>Description</h2>
            <p data-testid="system-description">
              <KeyInfo info={system.description} />
            </p>
            <div className="hanging-indent">
              <p>
                <span>Aliases: </span>
                {renderAliases(system.aliases)}
              </p>
            </div>
          </div>

          <div className="contentBlock">
            <h2>About</h2>
            <Link
              data-testid="about-change-link"
              className="change-link"
              to={window.location.pathname + `/update-about`}
            >
              Update
            </Link>
            {/* TODO discuss with team if better way to do relative path with react-router-dom */}
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Portfolio</GridCol>
              <GridCol setWidth="one-half">
                <KeyInfo info={system.portfolio} />
              </GridCol>
            </GridRow>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Criticality assessment</GridCol>
              <GridCol setWidth="one-half">
                <Criticality level={system.criticality} />
              </GridCol>
            </GridRow>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Investment state</GridCol>
              <GridCol setWidth="one-half">
                <InvestmentState state={system.investment_state} />
              </GridCol>
            </GridRow>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Developed by</GridCol>
              <GridCol setWidth="one-half">
                <KeyInfo info={system.developed_by} />
              </GridCol>
            </GridRow>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Supported by</GridCol>
              <GridCol setWidth="one-half">
                <KeyInfo info={system.supported_by} />
              </GridCol>
            </GridRow>
          </div>
          <div className="contentBlock">
            <h2>Contacts</h2>
            <Link
              data-testid="contacts-change-link"
              className="change-link"
              to={window.location.pathname + `/update-contacts`}
            >
              Update
            </Link>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Business owner</GridCol>
              <GridCol setWidth="one-half">
                <KeyInfo info={system.business_owner} />
              </GridCol>
            </GridRow>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Technical owner</GridCol>
              <GridCol setWidth="one-half">
                <KeyInfo info={system.tech_owner} />
              </GridCol>
            </GridRow>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Service owner</GridCol>
              <GridCol setWidth="one-half">
                <KeyInfo info={system.service_owner} />
              </GridCol>
            </GridRow>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Product owner</GridCol>
              <GridCol setWidth="one-half">
                <KeyInfo info={system.product_owner} />
              </GridCol>
            </GridRow>
            <GridRow className="system-view-row">
              <GridCol setWidth="one-quarter">Information asset owner</GridCol>
              <GridCol setWidth="one-half">
                <KeyInfo info={system.information_asset_owner} />
              </GridCol>
            </GridRow>
          </div>
          <h2>Risk</h2>
          <div data-testid="risk-container" className="riskContainer">
            {system.risks.map((risk) => (
              <RiskDetails key={risk.name} risk={risk} />
            ))}
          </div>
        </>
      ) : (
        <p>loading system data...</p>
      )}
    </div>
  );
}

SystemView.propTypes = {
  system: PropTypes.shape({
    name: PropTypes.string.isRequired,
    aliases: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    description: PropTypes.string,
    portfolio: PropTypes.string,
    criticality: PropTypes.string,
    investment_state: PropTypes.string,
    business_owner: PropTypes.string,
    service_owner: PropTypes.string,
    tech_owner: PropTypes.string,
    product_owner: PropTypes.string,
    information_asset_owner: PropTypes.string,
    developed_by: PropTypes.string,
    supported_by: PropTypes.string,
    risks: PropTypes.arrayOf(
      PropTypes.shape({
        name: PropTypes.string.isRequired,
        level: PropTypes.string,
        rationale: PropTypes.string,
      })
    ).isRequired,
    last_updated: PropTypes.shape({
      timestamp: PropTypes.string,
      author_name: PropTypes.string,
    }).isRequired,
  }),
  status: PropTypes.oneOf(["success"]),
  onClose: PropTypes.func.isRequired,
};

function renderAliases(aliases) {
  if (aliases.length > 0) {
    aliases.sort((a, b) => {
      return a.toLowerCase().localeCompare(b.toLowerCase());
    });
    return aliases.join(", ");
  } else return "This system is not known by another name.";
}

export default SystemView;
