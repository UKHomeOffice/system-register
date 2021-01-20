import React from 'react'
import { render } from "@testing-library/react";
import withRouting from '../withRouting';
import { Link, Route } from 'react-router-dom';
import userEvent from '@testing-library/user-event';

describe('withRouting', () => {

    const DummyComponent = () => {
        return (
            <>
                <Route path="/toFunkyTown">
                    <p>saxaphone!</p>
                </Route>
                <Link to="/toFunkyTown">oh won't you take me to</Link>
            </>
        )
    }

    it('renders', async () => {
        const RoutedComponent = withRouting(DummyComponent);

        const { queryByText } = render(<RoutedComponent />)

        expect(queryByText("saxaphone!")).not.toBeInTheDocument();
    });

    it('provides routing', async () => {
        const RoutedComponent = withRouting(DummyComponent);
        const { getByText } = render(<RoutedComponent />)

        userEvent.click(getByText(/oh won't you take me to/i))

        expect(getByText("saxaphone!")).toBeInTheDocument();
    });
});