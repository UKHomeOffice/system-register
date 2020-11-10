import riskConfig from './riskConfiguration'
import scoreCriticality from './scoreCriticality';

function summariseRisk(systems) {
    const riskSummaries = systems.map(s => summariseSystemRisk(s))
    const result = []

    riskSummaries.forEach(risks => {
        risks.forEach(risk => {
            if (result.filter(r => r.name === risk.name).length === 0) {
                result.push(risk)
            }
            else {
                let matchingRisk = result.find(r => r.name === risk.name)
                matchingRisk.knownSystems += risk.knownSystems
                matchingRisk.unknownSystems += risk.unknownSystems
                matchingRisk.knownRisk += risk.knownRisk
                matchingRisk.lowRiskSystems += risk.lowRiskSystems
                matchingRisk.mediumRiskSystems += risk.mediumRiskSystems
                matchingRisk.highRiskSystems += risk.highRiskSystems
                matchingRisk.unknownRisk += risk.unknownRisk
            }
        })
    });


    return result
}

function summariseSystemRisk(system) {
    return system.risks.map(risk => {
        return ({
            name: risk.name,
            knownSystems: riskConfig.isRiskKnown(risk.level) ? 1 : 0,
            unknownSystems: riskConfig.isRiskKnown(risk.level) ? 0 : 1,
            knownRisk: riskConfig.mapToKnownRisk(risk.level),
            lowRiskSystems: risk.level === 'low' ? 1 : 0,
            mediumRiskSystems: risk.level === 'medium' ? 1 : 0,
            highRiskSystems: risk.level === 'high' ? 1 : 0,
            unknownRisk: riskConfig.mapToUnknownRisk(risk.level)
        })
    })
}

function sumKnownRisk(system) {
    const riskValues = system.risks.map(r => riskConfig.mapToKnownRisk(r.level))
    const score = riskValues.reduce((a, b) => a + b)
    if(system.criticality) return score * scoreCriticality(system.criticality)
    return score
}

export { summariseSystemRisk, summariseRisk, sumKnownRisk }