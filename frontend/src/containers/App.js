import React from 'react'
import './App.css';
import SystemList from '../components/SystemList/SystemList'
import TitleBar from '../components/TitleBar/TitleBar'
import {BrowserRouter, Link, Route, Switch} from 'react-router-dom'
import System from '../components/System/System'
import Menu from '../components/Menu/Menu'
import PortfolioHeatmap from '../components/Visualisations/PortfolioHeatmap/PortfolioHeatmap';
import {KeycloakProvider} from '@react-keycloak/web'
import Banner from '../components/Banner/Banner'
import api from '../services/api';
import config from '../config/config';
import Keycloak from 'keycloak-js'
import SRFooter from "../components/SRFooter/SRFooter";

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

  componentWillUnmount() {
    this._isMounted = false;
  }

  render() {
    if (this.state.keycloak) {
      return (
        <KeycloakProvider keycloak={this.state.keycloak}>
          <BrowserRouter>
            <header>
              <TitleBar/>
              <Banner phase="in development">
                This is a new service - your <Link className="gds-link" to="/contact">feedback</Link> will help us to
                improve it.
              </Banner>
            </header>
            <div className="page">
            <Menu/>
            <main className="content-wrap">
              <Switch>
                <Route exact path="/">
                  <SystemList register={this.state.register}/>
                </Route>
                <Route path="/system/:id" component={System}/>
                <Route exact path="/risk_dashboard" component={PortfolioHeatmap}/>
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
