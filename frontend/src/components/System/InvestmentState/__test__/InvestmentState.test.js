import React from 'react'
import { cleanup, render } from '@testing-library/react'
import InvestmentState from '../InvestmentState'

afterEach(cleanup)

describe('<InvestmentState />', () => {
    it('makes evergreen look... evergreen!', () => {
        const { getByText } = render(<InvestmentState state="evergreen" />)
        const element = getByText('Evergreen')
        toLookEvergreen(element)
    });

    it('makes invest look like its invested in', () => {
        const { getByText } = render(<InvestmentState state="invest" />)
        const element = getByText('Invest')
        toLookInvested(element)
    });

    it('makes maintain look like its being maintained', () => {
        const { getByText } = render(<InvestmentState state="maintain" />)
        const element = getByText('Maintain')
        toLookMaintained(element)
    });

    it('makes sunset look like a beautfiul sunset', () => {
        const { getByText } = render(<InvestmentState state="sunset" />)
        const element = getByText('Sunset')
        toLookLikeASunset(element)
    });

    it('makes decomissioned tp look like its gone', () => {
        const { getByText } = render(<InvestmentState state="decommissioned" />)
        const element = getByText('Decommissioned')
        toLookLikeItsBeenDecommissioned(element)
    });

    it('makes unknown investment state look mysterious', () => {
        const { getByText } = render(<InvestmentState state="unknown" />)
        const element = getByText('Investment Unknown')
        toLookMysterious(element)
    });
})

function toLookEvergreen(element) {
    const html = `<span class="badge investEvergreen">Evergreen</span>`
    expect(element).toContainHTML(html)
}

function toLookInvested(element) {
    const html = `<span class="badge invest">Invest</span>`
    expect(element).toContainHTML(html)
}

function toLookMaintained(element) {
    const html = `<span class="badge investMaintain">Maintain</span>`
    expect(element).toContainHTML(html)
}

function toLookLikeASunset(element) {
    const html = `<span class="badge investSunset">Sunset</span>`
    expect(element).toContainHTML(html)
}

function toLookLikeItsBeenDecommissioned(element) {
    const html = `<span class="badge investDecommissioned">Decommissioned</span>`
    expect(element).toContainHTML(html)
}

function toLookMysterious(element) {
    const html = `<span class="badge investmentUnknown">Investment Unknown</span>`
    expect(element).toContainHTML(html)
}
