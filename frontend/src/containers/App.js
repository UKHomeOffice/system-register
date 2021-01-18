import React from 'react';
import SystemRegister from './SystemRegister/SystemRegister';
import withKeycloak from './withKeycloak'
import withRouting from './withRouting';

import './App.css';

const App = () => <SystemRegister />

export default withKeycloak(withRouting(App));