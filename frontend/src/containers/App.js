import React from 'react';
import Keycloak from 'keycloak-js';
import { KeycloakProvider } from '@react-keycloak/web';
import { BrowserRouter, Link, Route, Switch } from 'react-router-dom';

import About from '../components/About';
import Banner from '../components/Banner/Banner';
import Contact from '../components/Contact';
import Menu from '../components/Menu/Menu';
import PortfolioHeatmap from '../components/Visualisations/PortfolioHeatmap/PortfolioHeatmap';
import SRFooter from "../components/SRFooter/SRFooter";
import System from '../components/System/System';
import SystemList from '../components/SystemList/SystemList';
import TitleBar from '../components/TitleBar/TitleBar';
import api from '../services/api';
import config from '../config/config';

import './App.css';

// todo state should be in containers, components should be stateless
class App extends React.Component {
  state = {
    keycloak: undefined,
    register: {
      "systems": []
    }
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
            register: this.state.register
          })
      }).catch((e) => console.error(e))

    api.getAllSystems()
      .then((register) => {
        if (this._isMounted) {
          this.setState(
            {
              keycloak: this.state.keycloak,
              register: register
            })
        }
      })
  }

  duplicateNameCallback = (name) => {
    name = name.toLowerCase();
    const allSystemNames = this.state.register.systems.map(system => system.name.toLowerCase())
    return allSystemNames.includes(name);
  }

  componentWillUnmount() {
    this._isMounted = false;
  }

  render() {
    if (this.state.keycloak) {
      return (
        <KeycloakProvider keycloak={this.state.keycloak}>
          <BrowserRouter>
            <header>
              <TitleBar />
              <Banner phase="in development">
                This is a new service - your <Link className="gds-link" to="/contact">feedback</Link> will help us to
                improve it.
              </Banner>
            </header>
            <div className="page">
              <Menu />
              <main className="content-wrap">
                <Switch>
                  <Route exact path="/">
                    <SystemList register={this.state.register} />
                  </Route>
                  <Route path="/system/:id">
                    <System executeCheck={this.duplicateNameCallback}/>
                  </Route>
                  <Route exact path="/risk_dashboard" component={PortfolioHeatmap} />
                  <Route exact path="/about" component={About} />
                  <Route exact path="/contact" component={Contact} />
                </Switch>
              </main>
              <SRFooter/>
            </div>
          </BrowserRouter>
        </KeycloakProvider>
      );
    } else {
      return <main><p data-testid="auth-initialising-msg">Initialising authentication...</p></main>
    }
  }
}

export default App;
