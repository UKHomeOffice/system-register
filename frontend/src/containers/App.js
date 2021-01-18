import React from 'react';
import Keycloak from 'keycloak-js';
import { KeycloakProvider } from '@react-keycloak/web';
import { BrowserRouter } from 'react-router-dom';
import SystemRegister from './SystemRegister/SystemRegister';

import config from '../config/config';

import getPortfolios from './getPortfolios';

import './App.css';

// todo state should be in containers, components should be stateless
class App extends React.Component {
  state = {
    keycloak: undefined
  }

  componentDidMount() {
    this._isMounted = true;
    config.getKeycloakConfig()
      .then((c) => {
        const keycloakConfig = {
          url: c.url,
          realm: c.realm,
          clientId: c.clientId
        }
        this.setState(
          {
            keycloak: new Keycloak(keycloakConfig),
          })
      }).catch((e) => console.error(e))
  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  render() {
    if (this.state.keycloak) {
      return (
        <KeycloakProvider keycloak={this.state.keycloak}>
          <BrowserRouter>
           <SystemRegister />
          </BrowserRouter>
        </KeycloakProvider >
      );
    } else {
      return <main><p data-testid="auth-initialising-msg">Initialising authentication...</p></main>
    }
  }
}

export default App;