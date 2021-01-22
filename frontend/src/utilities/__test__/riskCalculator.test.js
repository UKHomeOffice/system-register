import { summariseRisk, sumKnownRisk } from '../riskCalculator'
import riskConfig from '../riskConfiguration'

const { RISK_VALUES } = riskConfig

const a_high_risk = (name = "roadmap") => ({ name: name, level: 'high' })
const a_low_risk = (name = "roadmap") => ({ name: name, level: 'low' })
const a_medium_risk = (name = "roadmap") => ({ name: name, level: 'medium' })
const an_unknown_risk = (name = "roadmap") => ({ name: name, level: 'unknown' })
const a_null_risk = (name = "roadmap") => ({ name: name, level: null })

const a_system = (id = 1, name = "Sys", risks = [a_low_risk()]) => ({ id: id, name: name, risks: risks })
const a_high_criticality_system = (id = 1, name = "Sys", risks = [a_low_risk()]) => ({ id: id, criticality: 'high', name: name, risks: risks })
const a_cni_system = (id = 1, name = "Sys", risks = [a_low_risk()]) => ({ id: id, criticality: 'cni', name: name, risks: risks })

describe('riskCalulator()', () => {
    describe('sumTotalKnownRisk(system)', () => {
        it('correctly sums value of a system with one low risk, risk lens', () => {
            // ∆ <- wow!
            expect(sumKnownRisk(a_system())).toEqual(RISK_VALUES.low)
        })
        it('correctly sums value of a system with no risk lenses', () => {
            const system = a_system()
            system.risks = []
            expect(sumKnownRisk(system)).toEqual(0)
        })
        it('correctly sums value of a system with one low risk and an unkown risk', () => {
            const system = a_system()
            system.risks.push(an_unknown_risk())
            expect(sumKnownRisk(system)).toEqual(RISK_VALUES.low)
        })
        it('correctly sums value of a system with one medium risk', () => {
            const system = a_system()
            system.risks = [a_medium_risk()]
            expect(sumKnownRisk(system)).toEqual(RISK_VALUES.medium)
        })
        it('correctly sums value of a system with two medium risk', () => {
            const system = a_system()
            system.risks = [a_medium_risk("x"), a_medium_risk("y")]
            expect(sumKnownRisk(system)).toEqual(RISK_VALUES.medium * 2)
        })
        it('correctly sums value of a system with all risks', () => {
            const system = a_system()
            system.risks = [a_low_risk("a"), a_medium_risk("b"), a_high_risk("c"), an_unknown_risk("d"), a_null_risk("e")]
            expect(sumKnownRisk(system))
                .toEqual(Object.values(RISK_VALUES).reduce((a, b) => a + b) - RISK_VALUES.unknown)
        })
    })

    describe('sumTotalKnownRisk(system, criticality)', () => {
        it('correctly sums value of a CNI system with one low risk', () => {
            expect(sumKnownRisk(a_cni_system())).toEqual(RISK_VALUES.low)
        })
        it('correctly sums value of a CNI system with one low risk and an unkown risk', () => {
            const system = a_cni_system()
            system.risks.push(an_unknown_risk())
            expect(sumKnownRisk(system)).toEqual(RISK_VALUES.low)
        })
        it('correctly sums value of a CNI system with one medium risk', () => {
            const system = a_cni_system()
            system.risks = [a_medium_risk()]
            expect(sumKnownRisk(system)).toEqual(RISK_VALUES.medium * 4)
        })
        it('correctly sums value of a CNI system with one low risk', () => {
            const system = a_cni_system()
            system.risks = [a_low_risk()]
            expect(sumKnownRisk(system)).toEqual(RISK_VALUES.low * 4)
        })
        it('correctly sums value of a High criticality system with one high risk', () => {
            const system = a_high_criticality_system()
            system.risks = [a_high_risk()]
            expect(sumKnownRisk(system)).toEqual(RISK_VALUES.high * 3)
        })
        it('correctly sums value of a CNI system with all risks', () => {
            const system = a_cni_system()
            system.risks = [a_low_risk("a"), a_medium_risk("b"), a_high_risk("c"), an_unknown_risk("d"), a_null_risk("e")]
            expect(sumKnownRisk(system))
                .toEqual((Object.values(RISK_VALUES).reduce((a, b) => a + b) - RISK_VALUES.unknown) * 4)
        })
    })

    it('calculates known risk summary for one system', () => {
        const system = a_system()
        system.risks.push(a_medium_risk('sunset'))

        const expected = [
            {
                name: 'roadmap',
                knownSystems: 1,
                unknownSystems: 0,
                knownRisk: RISK_VALUES.low,
                lowRiskSystems: 1,
                mediumRiskSystems: 0,
                highRiskSystems: 0,
                unknownRisk: 0
            },
            {
                name: 'sunset',
                knownSystems: 1,
                unknownSystems: 0,
                knownRisk: RISK_VALUES.medium,
                lowRiskSystems: 0,
                mediumRiskSystems: 1,
                highRiskSystems: 0,
                unknownRisk: 0
            }
        ]
        expect(summariseRisk([system])).toEqual(expected)
    })

    it('calculates known risk summary for two systems', () => {
        const systemA = a_system()
        const systemB = a_system()
        systemA.risks.push(a_medium_risk('sunset'))
        systemB.risks.push(a_medium_risk('sunset'))

        const expected = [
            {
                name: 'roadmap',
                knownSystems: 2,
                unknownSystems: 0,
                knownRisk: RISK_VALUES.low,
                lowRiskSystems: 2,
                mediumRiskSystems: 0,
                highRiskSystems: 0,
                unknownRisk: 0
            },
            {
                name: 'sunset',
                knownSystems: 2,
                unknownSystems: 0,
                knownRisk: RISK_VALUES.medium * 2,
                lowRiskSystems: 0,
                mediumRiskSystems: 2,
                highRiskSystems: 0,
                unknownRisk: 0
            }
        ]
        expect(summariseRisk([systemA, systemB])).toEqual(expected)
    })

    it('calculates compelex known risk summary for unknown systems', () => {
        const systemA = a_system()
        const systemB = a_system()
        const systemC = a_system()
        systemA.risks.push(an_unknown_risk('sunset'))
        systemB.risks.push(a_null_risk('sunset'))
        systemC.risks.push(a_high_risk('sunset'))

        const expected = [
            {
                name: 'roadmap',
                knownSystems: 3,
                unknownSystems: 0,
                knownRisk: RISK_VALUES.low * 2,
                lowRiskSystems: 3,
                mediumRiskSystems: 0,
                highRiskSystems: 0,
                unknownRisk: 0
            },
            {
                name: 'sunset',
                knownSystems: 1,
                unknownSystems: 2,
                knownRisk: RISK_VALUES.high,
                lowRiskSystems: 0,
                mediumRiskSystems: 0,
                highRiskSystems: 1,
                unknownRisk: RISK_VALUES.unknown * 2
            }
        ]
        expect(summariseRisk([systemA, systemB, systemC])).toEqual(expected)
    })
})