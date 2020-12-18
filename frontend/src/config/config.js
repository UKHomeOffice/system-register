import axios from "axios";

const baseUrl = process.env.NODE_ENV === "development"
  ? "http://localhost:8080"
  : "";

const config = {
  baseUrl,
  getKeycloakConfig,
  api: {
    url: `${baseUrl}/api`
  },
};

async function getKeycloakConfig() {
  const res = await axios.get(`${baseUrl}/config/keycloak`);
  return {
    url: res.data.host,
    realm: res.data.realm,
    clientId: res.data.clientId,
  };
}

export default config;
