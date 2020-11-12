import React from 'react'
import { render } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import UserEvents from '@testing-library/user-event'
import Auth from '../Auth'
import { useKeycloak } from '@react-keycloak/web'

jest.mock('@react-keycloak/web', () => ({
    useKeycloak: () => {
        console.log("Signing in...")
    }
}));

describe('<Auth />', () => {
    it('renders without crashing', () => {
        const { getByText } = render(<MemoryRouter><Auth /></MemoryRouter>)
        const element = getByText('Sign in')
        expect(element).toBeInTheDocument()
    });

    it('on click, asks keycloak to authenticate', () => {
        const { getByText } = render(<MemoryRouter><Auth /></MemoryRouter>)
        const element = getByText('Sign in')
        UserEvents.click(element)

        expect(useKeycloak).toBeCalled()
    })
})