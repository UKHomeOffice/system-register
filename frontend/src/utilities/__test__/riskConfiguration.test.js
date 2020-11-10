import riskConfig from '../riskConfiguration'

describe('mapping risk strings to numbers', () => {
    describe('riskConfig.mapToKnownRisk(str)', () => {
        it('maps high to correctValue', () => {
            expect(riskConfig.mapToKnownRisk('high')).toBe(riskConfig.RISK_VALUES.high)
        })

        it('maps medium to correctValue', () => {
            expect(riskConfig.mapToKnownRisk('medium')).toBe(riskConfig.RISK_VALUES.medium)
        })

        it('it maps medium to correctValue', () => {
            expect(riskConfig.mapToKnownRisk('low')).toBe(riskConfig.RISK_VALUES.low)
        })

        it('it maps not_applicable to correctValue', () => {
            expect(riskConfig.mapToKnownRisk('not_applicable')).toBe(riskConfig.RISK_VALUES.not_applicable)
        })

        it('it maps unknown to zero', () => {
            expect(riskConfig.mapToKnownRisk('unknown')).toBe(0)
        })

        it('it maps null to be zero', () => {
            expect(riskConfig.mapToKnownRisk(null)).toBe(0)
        })

        it('it maps undefined to be zero', () => {
            expect(riskConfig.mapToKnownRisk(undefined)).toBe(0)
        })

        it('it maps empty string to be zero', () => {
            expect(riskConfig.mapToKnownRisk('')).toBe(0)
        })

        it('throws error if risk not recognised', () => {
            expect(() => riskConfig.mapToKnownRisk('unexpected__')).toThrow()
        })
    })

    describe('riskConfig.mapToUnknownRisk(str)', () => {

        it('it maps unknown to correctValue', () => {
            expect(riskConfig.mapToUnknownRisk('unknown')).toBe(riskConfig.RISK_VALUES.unknown)
        })

        it('it maps null to correctValue', () => {
            expect(riskConfig.mapToUnknownRisk(null)).toBe(riskConfig.RISK_VALUES.unknown)
        })

        it('it maps undefined to correctValue', () => {
            expect(riskConfig.mapToUnknownRisk(undefined)).toBe(riskConfig.RISK_VALUES.unknown)
        })

        it('it maps empty string to correctValue', () => {
            expect(riskConfig.mapToUnknownRisk('')).toBe(riskConfig.RISK_VALUES.unknown)
        })


        it('maps high to zero', () => {
            expect(riskConfig.mapToUnknownRisk('high')).toBe(0)
        })

        it('maps medium to zero', () => {
            expect(riskConfig.mapToUnknownRisk('medium')).toBe(0)
        })

        it('it maps medium to zero', () => {
            expect(riskConfig.mapToUnknownRisk('low')).toBe(0)
        })

        it('it maps not_applicable to zero', () => {
            expect(riskConfig.mapToUnknownRisk('not_applicable')).toBe(0)
        })

        it('throws error if risk not recognised', () => {
            expect(() => riskConfig.mapToUnknownRisk('unexpected__')).toThrow()
        })
    })
})
