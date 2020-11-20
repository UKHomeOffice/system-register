import React from 'react'
import { cleanup, render } from '@testing-library/react'
import RiskBadge from '../RiskBadge'

afterEach(cleanup)

describe('<RiskBadge />', () => {
    it('makes high risk look risky', () => {
        const { getByText } = render(<RiskBadge level="high" />)
        const element = getByText('HIGH')
        toLookRisky(element)
    });

    it('makes medium risk look sort of risky', () => {
        const { getByText } = render(<RiskBadge level="medium" />)
        const element = getByText('MEDIUM')
        toLookMediumRisky(element)
    });

    it('makes low risk look sort of risky', () => {
        const { getByText } = render(<RiskBadge level="low" />)
        const element = getByText('LOW')
        toLookLowRisky(element)
    });

    it('makes not applicable risk look fine', () => {
        const { getByText } = render(<RiskBadge level="not_applicable" />)
        const element = getByText('N/A')
        toLookFine(element)
    });

    it('makes unknown risk look mysterious', () => {
        const { getByText } = render(<RiskBadge level="unknown" />)
        const element = getByText('UNKNOWN')
        toLookMysterious(element)
    });

})

function toLookRisky(element) {
    const html = `<span data-testid="risk-risk-badge-high" class="badge highRisk">HIGH</span>`
    expect(element).toContainHTML(html)
}

function toLookMediumRisky(element) {
    const html = `<span data-testid="risk-risk-badge-medium" class="badge mediumRisk">MEDIUM</span>`
    expect(element).toContainHTML(html)
}

function toLookLowRisky(element) {
    const html = `<span data-testid="risk-risk-badge-low" class="badge lowRisk">LOW</span>`
    expect(element).toContainHTML(html)
}

function toLookFine(element) {
    const html = `<span data-testid="risk-risk-badge-not_applicable" class="badge noRisk">N/A</span>`
    expect(element).toContainHTML(html)
}

function toLookMysterious(element) {
    const html = `<span data-testid="risk-risk-badge-unknown" class="badge unknownRisk">UNKNOWN</span>`
    expect(element).toContainHTML(html)
}

