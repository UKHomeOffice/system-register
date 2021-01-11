import React from 'react'
import './RiskBadge.css'
import toUpper from "../../../utilities/toUpper";

const NOT_APPLICABLE = 'not_applicable' //todo maybe centralise

const RiskBadge = (props) => {
    let riskClass;
    switch (props.level) {
        case 'high':
            riskClass = 'badge-highRisk'
            break;
        case 'medium':
            riskClass = 'badge-mediumRisk'
            break;
        case 'low':
            riskClass = 'badge-lowRisk'
            break;
        case NOT_APPLICABLE:
            riskClass = 'badge-noRisk'
            break;
        default:
            riskClass = 'badge-unknownRisk'
    }

    return <span className={`badge ${riskClass}`}>{formatLevel(props.level)}</span>
}

function formatLevel(level) {
    if (!level) return "UNKNOWN"
    if (level === NOT_APPLICABLE) {
        return 'N/A'
    }
    return toUpper(level)
}

export default RiskBadge
