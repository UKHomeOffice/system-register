import React from 'react'
import './System.css'
import UpdatedOn from '../UpdatedOn/UpdatedOn'
import KeyInfo from './KeyInfo/KeyInfo'
import RiskDetails from './RiskDetails/RiskDetails'
import Criticality from './Criticality/Criticality'
import InvestmentState from './InvestmentState/InvestmentState'
import Contact from './Contact/Contact'
import { CONTACT_TYPE } from './Contact/contactType'

const System = (props) => {

  const contactChanged = (args) => {
    console.log(args)
  }
  
  return (
    <div className="systemDetails centerContent">
      <h2>{props.system.name}</h2>
      <Criticality level={props.system.criticality} />
      <InvestmentState state={props.system.investment_state} />
      <ul>
        <li>Portfolio: <KeyInfo info={props.system.portfolio} /></li>
        <li><UpdatedOn date={props.system.last_updated} /></li>
        <li>Aliases: {renderAliases(props.system.aliases)}</li>
      </ul>
      <h3>Description</h3>
      <p><KeyInfo info={props.system.description} /></p>
      <h3>Contacts</h3>
      <ul>
        <li><Contact
          type={CONTACT_TYPE.REGISTER_OWNER}
          name={props.system.system_register_owner}
          onChange={contactChanged}
        />
        </li>
      </ul>
      <ul>
        <li>Developed By: <KeyInfo info={props.system.developed_by} /></li>
        <li>Supported By: <KeyInfo info={props.system.supported_by} /></li>
      </ul>
      <ul>
        <li><Contact type={CONTACT_TYPE.BUSINESS_OWNER} name={props.system.business_owner} /></li>
        <li><Contact type={CONTACT_TYPE.TECHNICAL_OWNER} name={props.system.technical_owner} /></li>
        <li><Contact type={CONTACT_TYPE.SERVICE_OWNER} name={props.system.service_owner} /></li>
        <li><Contact type={CONTACT_TYPE.PRODUCT_OWNER} name={props.system.product_owner} /></li>
        <li><Contact type={CONTACT_TYPE.INFORMATION_ASSET_OWNER} name={props.system.information_asset_owner} /></li>
      </ul>
      <h3>Risk</h3>
      <div className="riskContainer">
        {props.system.risks.map(risk => <RiskDetails key={risk.name} risk={risk} />)}
      </div>
    </div >
  )
}

function renderAliases(aliases) {
  if (aliases.length > 0)
    return aliases.join(', ')
  else
    return 'None'
}


export default System
