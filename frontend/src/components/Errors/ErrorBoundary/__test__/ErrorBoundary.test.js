import React from 'react'
import { cleanup, render } from '@testing-library/react'
import ErrorBoundary from '../ErrorBoundary';
import SystemNotFoundException from '../../../../services/systemNotFoundException';
import { BrowserRouter } from 'react-router-dom';

const ThrowsSystemNotFound = () => { throw new SystemNotFoundException() }
const ThrowsError = () => { throw new Error("Massive failure") }

afterEach(cleanup)

describe('<ErrorBoundary />', () => {
    beforeEach(() => {
        jest.spyOn(console, 'error')
        console.error.mockImplementation(() => null);
    });

    afterEach(() => {
        console.error.mockRestore()
    })

    it('catches SystemNotFoundException and displays <PageNotFoundError />', () => {
        const { getByText } = renderComponent(<ThrowsSystemNotFound />)

        const element = getByText('Page not found')

        expect(element).toBeInTheDocument()
    });

    it('catches other exceptions and displays <DefualtError />', () => {
        const { getByText } = renderComponent(<ThrowsError />)

        const element = getByText('Something went wrong...')

        expect(element).toBeInTheDocument()
    });
});

function renderComponent(component) {
    return render(
        <BrowserRouter>
            <ErrorBoundary>
                {component}
            </ErrorBoundary>
        </BrowserRouter>
    );
}
