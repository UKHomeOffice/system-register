import React from 'react'
import dummyData from '../data/systems_dummy.json'
import './App.css';
import SystemList from '../components/SystemList/SystemList'
import TitleBar from '../components/TitleBar/TitleBar'
import {BrowserRouter, Route, Switch} from 'react-router-dom'
import System from '../components/System/System'
import Menu from '../components/Menu/Menu'
import PortfolioHeatmap from '../components/Visualisations/PortfolioHeatmap/PortfolioHeatmap';
import {KeycloakProvider} from '@react-keycloak/web'
import keycloak from '../utilities/keycloak'
import axios from 'axios'
import config from '../config/config';

// todo state should be in containers, components should be stateless
class App extends React.Component {
    state = {
        register: {
            "systems": []
        }
    }

    componentDidMount() {
        this._isMounted = true;
        axios.get(`${config.api.url}/systems`)
            .then(res => {
                if (this._isMounted) {
                    console.log("Got system data")
                    this.setState({register: res.data});
                }
            })
            .catch(e => {
                console.error("Error calling api, displaying dummy data!: " + e)
            })
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    render() {
        return (
            <KeycloakProvider keycloak={keycloak}>
                <BrowserRouter>
                    <TitleBar/>
                    <Menu/>
                    <main>
                        <Switch>
                            <Route exact path="/">
                                <SystemList register={this.state.register}/>
                            </Route>
                            <Route exact path="/system/:id" render={({match}) => {
                                return <System
                                    system={this.state.register?.systems.find(s => s.id.toString() === match.params.id)}/>
                            }}>
                            </Route>
                            <Route exact path="/risk_dashboard">
                                <PortfolioHeatmap systems={this.state.register?.systems}/>
                            </Route>
                            {/* test system not found */}
                            {/* 404*/}
                        </Switch>
                    </main>
                </BrowserRouter>
            </KeycloakProvider>
        );
    }
}

export default App;
