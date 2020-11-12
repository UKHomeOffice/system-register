import React from 'react'
import { render, waitFor } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import UserEvents from '@testing-library/user-event'
import Auth from '../Auth'
import { useKeycloak } from '@react-keycloak/web'

jest.mock('@react-keycloak/web', () => ({
    useKeycloak: jest.fn(() => []),
}));

describe('<Auth />', () => {
    it('renders without crashing', () => {
        const { getByText } = render(<MemoryRouter><Auth /></MemoryRouter>)
        const element = getByText('Sign in')
        expect(element).toBeInTheDocument()
    });

    it('on click, asks keycloak to authenticate', () => {
        const login = jest.fn().mockResolvedValue(null);
        useKeycloak.mockReturnValue([{ login }]);

        const { getByText } = render(<MemoryRouter><Auth /></MemoryRouter>)
        const element = getByText('Sign in')
        UserEvents.click(element)

        expect(login).toBeCalled();
    })

    describe('when signed in', () => {
        beforeEach(() => {
            useKeycloak.mockReturnValue([{
                authenticated: true,
                tokenParsed: { preferred_username: 'corey' },
                token: 'bearer-token',
                logout: jest.fn().mockResolvedValue(null),
            }]);
        });

        it('has a sign out button', () => {
            const { getByRole } = render(<MemoryRouter><Auth /></MemoryRouter>)
            const element = getByRole('button', { name: 'Sign out' });

            expect(element).toBeInTheDocument();
        });

        it('welcomes the user', () => {
            const { getByText } = render(<MemoryRouter><Auth /></MemoryRouter>)
            const message = getByText('Welcome corey');

            expect(message).toBeInTheDocument();
        });

        it('stores the bearer token in local storage', () => {
            render(<MemoryRouter><Auth /></MemoryRouter>);

            const token = localStorage.getItem('bearer-token');

            expect(token).toBe('bearer-token');
        })

        it('clears the bearer token on signing out', () => {
            const { getByRole } = render(<MemoryRouter><Auth /></MemoryRouter>)
            const signOutButton = getByRole('button', { name: 'Sign out' });

            UserEvents.click(signOutButton);

            waitFor(() => {
                const token = localStorage.getItem('bearer-token');
                expect(token).toBeNull();
            });
        });
    })
})