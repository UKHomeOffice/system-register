import React from 'react'
import KeyInfo from '../KeyInfo/KeyInfo'
import toTitle from '../../../utilities/toTitle'
import RiskBadge from '../RiskBadge/RiskBadge'

const NOT_APPLICABLE = 'not_applicable'

const RiskDetails = (props) => {
  return (
    <div className='riskSummary'>
      <h4>{toTitle(props.risk.name)}</h4>
      <RiskBadge level={props.risk.level} />
      {renderRationale(props.risk.level, props.risk.rationale)}
    </div>
  )
}

function renderRationale(level, rationale) {
  if (level !== NOT_APPLICABLE) {
    return <p>Rationale: <KeyInfo info={rationale} /></p>
  }
}

export default RiskDetails
