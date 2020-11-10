import Keycloak from 'keycloak-js'
import config from '../config/config'

const keycloakConfig = {
    url: config.keycloak.url,
    realm: config.keycloak.realm,
    clientId: config.keycloak.clientId
}
const keycloak = new Keycloak(keycloakConfig);

export default keycloak