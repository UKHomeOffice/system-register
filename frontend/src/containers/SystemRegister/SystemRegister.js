import React from 'react';
import { Route, Switch } from 'react-router-dom';

import { ErrorBoundary } from 'react-error-boundary';

import About from '../../components/About';
import Banner from '../../components/Banner/Banner';
import Contact from '../../components/Contact';
import Link from '../../components/Linking/Link';
import Menu from '../../components/Menu/Menu';
import PortfolioHeatmap from '../../components/Visualisations/PortfolioHeatmap/PortfolioHeatmap';
import SRFooter from "../../components/SRFooter/SRFooter";
import System from '../../components/System/System';
import SystemList from '../../components/SystemList/SystemList';
import TitleBar from '../../components/TitleBar/TitleBar';
import PageNotFoundError from '../../components/Errors/PageNotFoundError';

import getPortfolios from '../getPortfolios';
import api from '../../services/api';


class SystemRegister extends React.Component {
    state = {
        register: {
            "systems": []
        }
    }

    loadSystems = async () => api.getAllSystems()
        .then((register) => {
            if (this._isMounted) {
                this.setState(
                    {
                        register: register
                    });
            }
        });

    componentDidMount() {
        this._isMounted = true;
        this.setState(
            {
                register: this.state.register
            })

        this.loadSystems()
    }

    checkForDuplicateNames = (name) => {
        name = name.toLowerCase();
        const allSystemNames = this.state.register.systems.map(system => system.name.toLowerCase())
        return allSystemNames.includes(name);
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    render() {
        return (<>
            <header>
                <TitleBar />
                <Banner phase="in development">
                    This is a new service - your <Link to="/contact">feedback</Link> will help us to improve it. </Banner>
            </header>
            <div className="page">
                <Menu />
                <main className="content-wrap">
                    <Switch>
                        <Route exact path="/">
                            <SystemList register={this.state.register} />
                        </Route>
                        <Route path="/system/:id">
                            <ErrorBoundary FallbackComponent={PageNotFoundError}>
                                <System portfolios={getPortfolios(this.state.register.systems)} onBeforeNameChange={this.checkForDuplicateNames} onChange={this.loadSystems} />
                            </ErrorBoundary>
                        </Route>
                        <Route exact path="/risk_dashboard" component={PortfolioHeatmap} />
                        <Route exact path="/about" component={About} />
                        <Route exact path="/contact" component={Contact} />
                        <Route path="/*" component={PageNotFoundError} />
                    </Switch>
                </main>
                <SRFooter />
            </div>
        </>
        )
    }
}

export default SystemRegister;