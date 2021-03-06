import { cleanup, render } from '@testing-library/react'
import KeyInfo from '../KeyInfo'
import React from 'react'


afterEach(cleanup)

describe('<KeyInfo />', () => {
    it('renders known value', () => {
        const { getByText } = render(<KeyInfo info="known value" />)
        const element = getByText('known value')
        expect(element).toBeInTheDocument()
    });

    it('renders empty value as Unknown', () => {
        const { getByText } = render(<KeyInfo info="" />)
        const element = getByText('UNKNOWN')
        toLookMysterious(element, 'UNKNOWN')
    });

    it('renders undefined value as Unknown', () => {
        const { getByText } = render(<KeyInfo info={undefined} />)
        const element = getByText('UNKNOWN')
        toLookMysterious(element, 'UNKNOWN')
    });
})

function toLookMysterious(element, str) {
    const html = `<strong class="unknownKeyInfo">${str}</strong>`
    expect(element).toContainHTML(html)
}
