import React from 'react'
import { cleanup, render } from '@testing-library/react'
import Criticality from '../Criticality'

afterEach(cleanup)

describe('<Criticality />', () => {
    it('makes CNI criticality look super important', () => {
        const { getByText } = render(<Criticality level="cni" />)
        const element = getByText('Criticality CNI')
        toLookSuperImportant(element)
    });

    it('makes high criticality look important', () => {
        const { getByText } = render(<Criticality level="high" />)
        const element = getByText('Criticality High')
        toLookImportant(element)
    });

    it('makes medium criticality look sort of important', () => {
        const { getByText } = render(<Criticality level="medium" />)
        const element = getByText('Criticality Medium')
        toLookSortOfImportant(element)
    });

    it('makes low criticality look unimportant', () => {
        const { getByText } = render(<Criticality level="low" />)
        const element = getByText('Criticality Low')
        toLookUnimportant(element)
    });

    it('makes unknown criticality look mysterious', () => {
        const { getByText } = render(<Criticality level="unknown" />)
        const element = getByText('Criticality Unknown')
        toLookMysterious(element)
    });

    it('hides label', () => {
        const { getByText } = render(<Criticality level="low" hideLabel={true} />)
        const element = getByText('Low')
        expect(element).toBeInTheDocument()
    });
})

function toLookSuperImportant(element) {
    const html = `<span class="badge criticalityCNI">Criticality CNI</span>`
    expect(element).toContainHTML(html)
}

function toLookImportant(element) {
    const html = `<span class="badge criticalityHigh">Criticality High</span>`
    expect(element).toContainHTML(html)
}

function toLookSortOfImportant(element) {
    const html = `<span class="badge criticalityMedium">Criticality Medium</span>`
    expect(element).toContainHTML(html)
}

function toLookUnimportant(element) {
    const html = `<span class="badge criticalityLow">Criticality Low</span>`
    expect(element).toContainHTML(html)
}

function toLookMysterious(element) {
    const html = `<span class="badge criticalityUnknown">Criticality Unknown</span>`
    expect(element).toContainHTML(html)
}
