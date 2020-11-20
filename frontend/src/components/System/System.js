import React from 'react'
import './System.css'
import KeyInfo from './KeyInfo/KeyInfo'
import RiskDetails from './RiskDetails/RiskDetails'
import Criticality from './Criticality/Criticality'
import InvestmentState from './InvestmentState/InvestmentState'
import {GridCol, GridRow} from "govuk-react";
import UpdatedOn from "../UpdatedOn/UpdatedOn";

const System = (props) => {

    return (
        <div className="systemDetails centerContent">
            <div className="contentBlock">
                <h1>{props.system.name}</h1>
                <p data-testid="system-last-modified"><UpdatedOn date={props.system.last_updated}/></p>
                <h2>Description</h2>
                <p data-testid="system-description" ><KeyInfo info={props.system.description}/></p>
            </div>
            <div className="contentBlock">
                <h2>About</h2>
                <GridRow>
                    <GridCol setWidth="one-quarter">
                        <p>Aliases</p>
                        <p>Department</p>
                        <p>Criticality</p>
                        <p>Investment state</p>
                        <p>Developed by</p>
                        <p>Supported by</p>
                    </GridCol>
                    <GridCol setWidth="one-half">
                        <p data-testid="about-aliases">{renderAliases(props.system.aliases)}</p>
                        <p data-testid="about-department"><KeyInfo info={props.system.department} /></p>
                        <p data-testid="about-criticality"><Criticality level={props.system.criticality} /> </p>
                        <p data-testid="about-investment-state"><InvestmentState state={props.system.investment_state} /></p>
                        <p data-testid="about-developed-by"><KeyInfo info={props.system.developed_by} /></p>
                        <p data-testid="about-supported-by"><KeyInfo info={props.system.supported_by} /></p>
                    </GridCol>
                </GridRow>
            </div>
            <div className="contentBlock">
                <h2>Contacts</h2>
                <GridRow>
                    <GridCol setWidth="one-quarter">
                        <p>System register owner</p>
                        <p>Business owner</p>
                        <p>Technical owner</p>
                        <p>Service owner</p>
                        <p>Product owner</p>
                        <p>Information asset owner</p>
                    </GridCol>
                    <GridCol setWidth="one-half">
                        <p data-testid="contacts-system-register-owner"><KeyInfo info={props.system.system_register_owner} /></p>
                        <p data-testid="contacts-business-owner"><KeyInfo info={props.system.business_owner} /></p>
                        <p data-testid="contacts-technical-owner"><KeyInfo info={props.system.technical_owner} /></p>
                        <p data-testid="contacts-service-owner"><KeyInfo info={props.system.service_owner} /></p>
                        <p data-testid="contacts-product-owner"><KeyInfo info={props.system.product_owner} /></p>
                        <p data-testid="contacts-information-asset-owner"><KeyInfo info={props.system.information_asset_owner} /></p>
                    </GridCol>
                </GridRow>
            </div>
            <h2>Risk</h2>
            <div data-testid="risk-container" className="riskContainer">
                {props.system.risks.map(risk => <RiskDetails key={risk.name} risk={risk}/>)}
            </div>
        </div>
    )
}

function renderAliases(aliases) {
    if (aliases.length > 0)
        return aliases.join(', ')
    else
        return 'None'
}


export default System
