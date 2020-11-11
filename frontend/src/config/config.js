const config = {
    keycloak: {
        url: process.env.REACT_APP_KEYCLOAK_URL || "unset-url",
        realm: process.env.REACT_APP_KEYCLOAK_REALM || "unset-realm",
        clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID || "unset-client-id",
    },
    api: {
        url: `${window.location.origin}/api`
    }
}

export default config