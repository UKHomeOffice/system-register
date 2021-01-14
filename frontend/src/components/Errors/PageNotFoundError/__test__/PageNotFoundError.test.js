import React from 'react'
import { cleanup, render } from '@testing-library/react'
import PageNotFoundError from '../PageNotFoundError';
import { BrowserRouter } from 'react-router-dom';

afterEach(cleanup)

describe('<PageNotFoundError />', () => {
    it('renders', () => {
        const { getByText } = render(<BrowserRouter><PageNotFoundError /></BrowserRouter>)
        const element = getByText('Page not found')
        expect(element).toBeInTheDocument()
    });
});