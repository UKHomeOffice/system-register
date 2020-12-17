import axios from 'axios'

const config = {
  getKeycloakConfig,
  api: {
    // url: `http://localhost:8080/api`
    url: `${window.location.origin}/api`
  },
}

async function getKeycloakConfig() {
  // const res = await axios.get('http://localhost:8080/config/keycloak');
  const res = await axios.get('/config/keycloak');
  return {
    url: res.data.host,
    realm: res.data.realm,
    clientId: res.data.clientId
  }
}

export default config