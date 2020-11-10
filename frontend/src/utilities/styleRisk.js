import * as d3 from 'd3'

const RISK_CLASS_MAP = {
    'unknown': 'unknownRisk',
    'not_applicable': 'notApplicableRisk',
    'low': 'lowRisk',
    'mediumLow': 'mediumLowRisk',
    'medium': 'mediumRisk',
    'mediumHigh': 'mediumHighRisk',
    'high': 'highRisk'
}

const styleRisk = (val) => val ? RISK_CLASS_MAP[val] : 'unknownRisk'

const styleRiskBackground = (val) => styleRisk(val) + 'Background'

const styleRiskScore = (min, max, val) => {
    if (val == null || val === undefined) return 'unknownRisk'
    const ramp = d3
        .scaleQuantize()
        .domain([min, max])
        .range(Object.values(RISK_CLASS_MAP).slice(2))
    return ramp(val)
}

const styleRiskScoreBackground = (min, max, val) => styleRiskScore(min, max, val) + 'Background'

export { RISK_CLASS_MAP, styleRisk, styleRiskBackground, styleRiskScore, styleRiskScoreBackground }