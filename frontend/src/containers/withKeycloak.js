import React, { useEffect, useState } from 'react'
import Keycloak from 'keycloak-js';
import { KeycloakProvider } from '@react-keycloak/web';
import config from '../config/config';


const withKeycloak = (Component) => {
    const KeycloakWrapped = (...props) => {
        var [keycloak, setKeycloak] = useState(undefined)
        useEffect(() => {
            config.getKeycloakConfig()
                .then((c) => {
                    const keycloakConfig = {
                        url: c.url,
                        realm: c.realm,
                        clientId: c.clientId
                    }
                    setKeycloak(new Keycloak(keycloakConfig))
                }).catch((e) => console.error(e))
        }, [])

        if (keycloak) {
            return <KeycloakProvider keycloak={keycloak}><> <Component {...props} /></></KeycloakProvider>
        } else {
            return <main><p data-testid="auth-initialising-msg">Initialising authentication...</p></main>
        }
    }

    return KeycloakWrapped
}

export default withKeycloak;