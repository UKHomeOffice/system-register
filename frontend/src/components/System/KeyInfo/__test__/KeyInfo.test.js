import React from 'react'
import { cleanup, render } from '@testing-library/react'
import KeyInfo from '../KeyInfo'

afterEach(cleanup)

describe('<KeyInfo />', () => {
    it('renders known value', () => {
        const { getByText } = render(<KeyInfo info="known value" />)
        const element = getByText('known value')
        expect(element).toBeInTheDocument()
    });

    it('renders empty value as Unknown', () => {
        const { getByText } = render(<KeyInfo info="" />)
        const element = getByText('Unknown')
        toLookMysterious(element, 'Unknown')
    });

    it('renders undefined value as Unknown', () => {
        const { getByText } = render(<KeyInfo info={undefined} />)
        const element = getByText('Unknown')
        toLookMysterious(element, 'Unknown')
    });
})

function toLookMysterious(element, str) {
    const html = `<strong class="highRisk">${str}</strong>`
    expect(element).toContainHTML(html)
}
