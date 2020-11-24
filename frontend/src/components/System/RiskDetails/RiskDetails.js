import React from 'react'
import KeyInfo from '../KeyInfo/KeyInfo'
import toTitle from '../../../utilities/toTitle'
import RiskBadge from '../RiskBadge/RiskBadge'

const NOT_APPLICABLE = 'not_applicable'

const RiskDetails = (props) => {
  return (
    <div data-testid="risk-details" className='riskSummary'>
      <h3>{toTitle(props.risk.name)}</h3>
      <RiskBadge level={props.risk.level} />
      {renderRationale(props.risk.level, props.risk.rationale, props.risk.name)}
    </div>
  )
}

function renderRationale(level, rationale, name) {
  if (level !== NOT_APPLICABLE) {
    return <p data-testid={`risk-rationale-${name}`}>Rationale: <KeyInfo info={rationale} /></p>
  }
}

export default RiskDetails
