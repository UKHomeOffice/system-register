import React from 'react'
import './RiskBadge.css'
import toTitle from '../../../utilities/toTitle'

const NOT_APPLICABLE = 'not_applicable' //todo maybe centralise

const RiskBadge = (props) => {
  const prefix = props.hideLabel ? "": "Risk: "
    let riskClass = "unkownRiskBackground"
    switch (props.level) {
        case 'high':
            riskClass = 'highRiskBackground'
            break;
        case 'medium':
            riskClass = 'mediumRiskBackground'
            break;
        case 'low':
            riskClass = 'lowRiskBackground'
            break;
        case NOT_APPLICABLE:
            riskClass = 'noRiskBackground'
            break;
        default:
            riskClass = 'unknownRiskBackground'
    }

    return <span className={`badge ${riskClass}`}>{prefix}{formatLevel(props.level)}</span>
}

function formatLevel(level) {
    if (!level) return "Unknown"
    if (level === NOT_APPLICABLE) {
        return 'n/a'
    }
    return toTitle(level)
}

export default RiskBadge
