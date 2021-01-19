import React from 'react'
import { render, act, waitForElementToBeRemoved } from "@testing-library/react";
import withKeycloak from '../withKeycloak';

describe('withKeycloak', () => {

    const DummyComponent = () => { return <div><p>I am wrapped component</p></div> }
    const mock_getKeycloakConfig = Promise.resolve({url: "mock_url", clientId:"mock_clientId", realm: "mock_realm"})
    const mockConfig = {
        baseUrl: "base_url",
        getKeycloakConfig: jest.fn(() => mock_getKeycloakConfig),
        api: {
            url: "base_url/api"
        },
    };

    it('shows initializing message on load', async () => {
        const SecureComponent = withKeycloak(mockConfig, DummyComponent);

        const { getByTestId } = render(<SecureComponent />)
        const initializingKeycloakMessage = getByTestId("auth-initialising-msg");

        expect(initializingKeycloakMessage).toBeInTheDocument();
        await act(() => mock_getKeycloakConfig)
    });

    it('asks for its configuration', async () => {
        const SecureComponent = withKeycloak(mockConfig, DummyComponent);

        const { getByTestId } = render(<SecureComponent />)
        const initializingKeycloakMessage = getByTestId("auth-initialising-msg");

        expect(initializingKeycloakMessage).toBeInTheDocument();
        await act(() => mock_getKeycloakConfig)
    });

    it('renders wrapped component once initialised', async () => {
        const SecureComponent = withKeycloak(mockConfig, DummyComponent);

        const { getByTestId, getByText} = render(<SecureComponent />)
        await waitForElementToBeRemoved(getByTestId("auth-initialising-msg"))

        const wrappedComponent = getByText("I am wrapped component")

        expect(wrappedComponent).toBeInTheDocument();
    });

});