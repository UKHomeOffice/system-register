const config = {
    keycloak: {
        url: process.env.REACT_APP_KEYCLOAK_URL || "http://localhost:8081/auth/",
        realm: process.env.REACT_APP_KEYCLOAK_REALM || "local-realm",
        clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID || "system-register",
    },
    api: {
        // url: `${window.location.origin}/api`
        url: `http://localhost:8080/api`
    }
}

export default config