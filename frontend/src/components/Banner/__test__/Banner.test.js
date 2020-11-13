import React from 'react'
import { render } from '@testing-library/react'
import Banner from '../Banner'

describe('<Banner />', () => {
    it('renders without crashing', () => {
        const { getByText } = render(<Banner phase="in development">phase message</Banner>)
        const element = getByText('phase message')
        expect(element).toBeInTheDocument()
    });

    it('renders an In Development badge', () => {
        const { getByText } = render(<Banner phase="In Development">phase message</Banner>)
        const element = getByText('In Development')
        expect(element).toBeInTheDocument()
    });
})