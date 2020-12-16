import axios from 'axios'

const config = {
  getKeycloakConfig,
  api: {
    url: `${window.location.origin}/api`
  },
}

async function getKeycloakConfig() {
  const res = await axios.get('/config/keycloak');
  return {
    url: res.data.host,
    realm: res.data.realm,
    clientId: res.data.clientId
  }
}

export default config