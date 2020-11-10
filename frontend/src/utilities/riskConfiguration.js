const RISK_VALUES = { //todo rename to ...SCORE
    'high': 2,
    'medium': 1,
    'low': 0,
    'unknown': 2,
    'not_applicable': 0
}

const RISK_CLASSES = [
    "lowRisk",
    "mediumLowRisk",
    "mediumRisk",
    "mediumHighRisk",
    "highRisk"]

function isRiskKnown(str) {
    if (str === 'unknown') return false
    if (str === '') return false
    if (str === null) return false
    if (str === undefined) return false
    if (RISK_VALUES[str] === undefined)
        throw Error("Unrecognized risk value: " + str)
    return true;
}

export default {

    RISK_CLASSES: RISK_CLASSES,
    RISK_VALUES: RISK_VALUES,

    mapToKnownRisk: (str) => {
        if (!isRiskKnown(str)) return 0
        const result = RISK_VALUES[str]
        if (result > -1)
            return result
        throw Error("Unrecognized risk value: " + result)
    },

    isRiskKnown: isRiskKnown,

    mapToUnknownRisk: (str) => {
        if (isRiskKnown(str)) return 0
        return RISK_VALUES.unknown;
    }
}