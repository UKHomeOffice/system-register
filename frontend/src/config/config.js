const config = {
    keycloak: {
        url: process.env.REACT_APP_KEYCLOAK_URL,
        realm: process.env.REACT_APP_KEYCLOAK_REALM,
        clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID,
    },
    api: {
        url: `${window.location.origin}/api`
    }
}

export default config