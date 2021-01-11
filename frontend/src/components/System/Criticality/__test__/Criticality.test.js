import React from 'react'
import { cleanup, render } from '@testing-library/react'
import Criticality from '../Criticality'

afterEach(cleanup)

describe('<Criticality />', () => {
    it('makes CNI criticality look super important', () => {
        const { getByText } = render(<Criticality level="cni" />)
        const element = getByText('CNI')
        toLookSuperImportant(element)
    });

    it('makes high criticality look important', () => {
        const { getByText } = render(<Criticality level="high" />)
        const element = getByText('HIGH')
        toLookImportant(element)
    });

    it('makes medium criticality look sort of important', () => {
        const { getByText } = render(<Criticality level="medium" />)
        const element = getByText('MEDIUM')
        toLookSortOfImportant(element)
    });

    it('makes low criticality look unimportant', () => {
        const { getByText } = render(<Criticality level="low" />)
        const element = getByText('LOW')
        toLookUnimportant(element)
    });

    it('makes unknown criticality look mysterious', () => {
        const { getByText } = render(<Criticality level="unknown" />)
        const element = getByText('UNKNOWN')
        toLookMysterious(element)
    });

})

function toLookSuperImportant(element) {
    const html = `<span class="badge criticality-cni">CNI</span>`
    expect(element).toContainHTML(html)
}

function toLookImportant(element) {
    const html = `<span class="badge criticality-high">HIGH</span>`
    expect(element).toContainHTML(html)
}

function toLookSortOfImportant(element) {
    const html = `<span class="badge criticality-medium">MEDIUM</span>`
    expect(element).toContainHTML(html)
}

function toLookUnimportant(element) {
    const html = `<span class="badge criticality-low">LOW</span>`
    expect(element).toContainHTML(html)
}

function toLookMysterious(element) {
    const html = `<span class="badge criticality-unknown">UNKNOWN</span>`
    expect(element).toContainHTML(html)
}
