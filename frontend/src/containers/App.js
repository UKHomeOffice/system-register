import React from 'react'
import './App.css';
import SystemList from '../components/SystemList/SystemList'
import TitleBar from '../components/TitleBar/TitleBar'
import { BrowserRouter, Link, Route, Switch } from 'react-router-dom'
import System from '../components/System/System'
import Menu from '../components/Menu/Menu'
import PortfolioHeatmap from '../components/Visualisations/PortfolioHeatmap/PortfolioHeatmap';
import { KeycloakProvider } from '@react-keycloak/web'
import keycloak from '../utilities/keycloak'
import Banner from '../components/Banner/Banner'
import api from '../services/api';

// todo state should be in containers, components should be stateless
class App extends React.Component {
    state = {
        register: {
            "systems": []
        }
    }

    componentDidMount() {
        this._isMounted = true;
        api.getAllSystems()
            .then((register) => {
                if (this._isMounted) {
                    this.setState({ register: register })
                }
            })
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    render() {
        return (
            <KeycloakProvider keycloak={keycloak}>
                <BrowserRouter>
                    <header>
                        <TitleBar />
                        <Banner phase="in development">
                            This is a new service - your <Link className="gds-link" to="/contact">feedback</Link> will help us to improve it.
                    </Banner>
                    </header>
                    <Menu />
                    <main>
                        <Switch>
                            <Route exact path="/">
                                <SystemList register={this.state.register} />
                            </Route>
                            <Route exact path="/system/:id" component={System} />
                            <Route exact path="/risk_dashboard">
                                <PortfolioHeatmap systems={this.state.register.systems} />
                            </Route>
                        </Switch>
                    </main>
                </BrowserRouter>
            </KeycloakProvider>
        );
    }
}

export default App;
