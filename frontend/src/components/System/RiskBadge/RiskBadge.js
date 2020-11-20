import React from 'react'
import './RiskBadge.css'
import toUpper from "../../../utilities/toUpper";

const NOT_APPLICABLE = 'not_applicable' //todo maybe centralise

const RiskBadge = (props) => {
    let riskClass;
    switch (props.level) {
        case 'high':
            riskClass = 'highRisk'
            break;
        case 'medium':
            riskClass = 'mediumRisk'
            break;
        case 'low':
            riskClass = 'lowRisk'
            break;
        case NOT_APPLICABLE:
            riskClass = 'noRisk'
            break;
        default:
            riskClass = 'unknownRisk'
    }

    return <span data-testid={`risk-risk-badge-${props.level}`} className={`badge ${riskClass}`}>{formatLevel(props.level)}</span>
}

function formatLevel(level) {
    if (!level) return "UNKNOWN"
    if (level === NOT_APPLICABLE) {
        return 'N/A'
    }
    return toUpper(level)
}

export default RiskBadge
