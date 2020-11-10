import React from 'react'
import { cleanup, render } from '@testing-library/react'
import RiskBadge from '../RiskBadge'

afterEach(cleanup)

describe('<RiskBadge />', () => {
    it('makes high risk look risky', () => {
        const { getByText } = render(<RiskBadge level="high" />)
        const element = getByText('Risk: High')
        toLookRisky(element)
    });

    it('makes medium risk look sort of risky', () => {
        const { getByText } = render(<RiskBadge level="medium" />)
        const element = getByText('Risk: Medium')
        toLookMediumRisky(element)
    });

    it('makes low risk look sort of risky', () => {
        const { getByText } = render(<RiskBadge level="low" />)
        const element = getByText('Risk: Low')
        toLookLowRisky(element)
    });

    it('makes not applicable risk look fine', () => {
        const { getByText } = render(<RiskBadge level="not_applicable" />)
        const element = getByText('Risk: n/a')
        toLookFine(element)
    });

    it('makes unknown risk look mysterious', () => {
        const { getByText } = render(<RiskBadge level="unknown" />)
        const element = getByText('Risk: Unknown')
        toLookMysterious(element)
    });

    it('hides preceeding label', () => {
        const { getByText } = render(<RiskBadge level="unknown" hideLabel={true}/>)
        const element = getByText('Unknown')
        expect(element).toBeInTheDocument()
    });
})

function toLookRisky(element) {
    const html = `<span class="badge highRiskBackground">Risk: High</span>`
    expect(element).toContainHTML(html)
}

function toLookMediumRisky(element) {
    const html = `<span class="badge mediumRiskBackground">Risk: Medium</span>`
    expect(element).toContainHTML(html)
}

function toLookLowRisky(element) {
    const html = `<span class="badge lowRiskBackground">Risk: Low</span>`
    expect(element).toContainHTML(html)
}

function toLookFine(element) {
    const html = `<span class="badge noRiskBackground">Risk: n/a</span>`
    expect(element).toContainHTML(html)
}

function toLookMysterious(element) {
    const html = `<span class="badge unknownRiskBackground">Risk: Unknown</span>`
    expect(element).toContainHTML(html)
}

