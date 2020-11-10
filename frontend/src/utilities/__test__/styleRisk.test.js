import { styleRisk, styleRiskBackground, styleRiskScore, styleRiskScoreBackground } from '../styleRisk'

describe('styling risk level', () => {
    it('styles high risk', () => {
        expect(styleRisk('high')).toEqual('highRisk')
    })
    it('styles medium risk', () => {
        expect(styleRisk('medium')).toEqual('mediumRisk')
    })
    it('styles low risk', () => {
        expect(styleRisk('low')).toEqual('lowRisk')
    })
    it('styles unknown risk', () => {
        expect(styleRisk('unknown')).toEqual('unknownRisk')
    })
    it('styles n/a risk', () => {
        expect(styleRisk('not_applicable')).toEqual('notApplicableRisk')
    })
    it('styles null risk', () => {
        expect(styleRisk(null)).toEqual('unknownRisk')
    })
    describe('styling backGrounds', () => {
        it('styles high risk', () => {
            expect(styleRiskBackground('high')).toEqual('highRiskBackground')
        })
        it('styles medium risk', () => {
            expect(styleRiskBackground('medium')).toEqual('mediumRiskBackground')
        })
        it('styles low risk', () => {
            expect(styleRiskBackground('low')).toEqual('lowRiskBackground')
        })
        it('styles unknown risk', () => {
            expect(styleRiskBackground('unknown')).toEqual('unknownRiskBackground')
        })
        it('styles n/a risk', () => {
            expect(styleRiskBackground('not_applicable')).toEqual('notApplicableRiskBackground')
        })
        it('styles null risk', () => {
            expect(styleRiskBackground(null)).toEqual('unknownRiskBackground')
        })
    })
})

describe('styling risk score', () => {
    it('styles null risk', () => {
        expect(styleRiskScore(0, 4, null)).toEqual('unknownRisk')
    })
    it('styles low risk', () => {
        expect(styleRiskScore(0, 4, 0)).toEqual('lowRisk')
    })
    it('styles mediumLow risk', () => {
        expect(styleRiskScore(0, 4, 1)).toEqual('mediumLowRisk')
    })
    it('styles medium risk', () => {
        expect(styleRiskScore(0, 4, 2)).toEqual('mediumRisk')
    })
    it('styles mediumHigh risk', () => {
        expect(styleRiskScore(0, 4, 3)).toEqual('mediumHighRisk')
    })
    it('styles high risk', () => {
        expect(styleRiskScore(0, 4, 4)).toEqual('highRisk')
    })
    describe('styling backGrounds', () => {
        it('styles null risk', () => {
            expect(styleRiskScoreBackground(0, 4, null)).toEqual('unknownRiskBackground')
        })
        it('styles low risk', () => {
            expect(styleRiskScoreBackground(0, 4, 0)).toEqual('lowRiskBackground')
        })
        it('styles mediumLow risk', () => {
            expect(styleRiskScoreBackground(0, 4, 1)).toEqual('mediumLowRiskBackground')
        })
        it('styles medium risk', () => {
            expect(styleRiskScoreBackground(0, 4, 2)).toEqual('mediumRiskBackground')
        })
        it('styles mediumHigh risk', () => {
            expect(styleRiskScoreBackground(0, 4, 3)).toEqual('mediumHighRiskBackground')
        })
        it('styles high risk', () => {
            expect(styleRiskScoreBackground(0, 4, 4)).toEqual('highRiskBackground')
        })
    })
})