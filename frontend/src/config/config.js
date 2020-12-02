import axios from 'axios'

const config = {
    getKeycloakConfig,
    keycloak: {
        url: process.env.REACT_APP_KEYCLOAK_URL || "http://localhost:8081/auth/",
        realm: process.env.REACT_APP_KEYCLOAK_REALM || "local-realm",
        clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID || "system-register",
    },
    api: {
        url: `${window.location.origin}/api`
    },
}

async function getKeycloakConfig() {
    const res = await axios.get(`/config/keycloak`);
    return {
        url: res.data.host,
        realm: res.data.realm,
        clientId: res.data.clientId
    }
}

export default config