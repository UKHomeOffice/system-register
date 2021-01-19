import React from 'react';
import SystemRegister from './SystemRegister/SystemRegister';
import withKeycloak from './withKeycloak'
import withRouting from './withRouting';
import { flow } from 'lodash-es'
import config from '../config/config';

import './App.css';

const App = () => <SystemRegister />

export default flow (
  withKeycloak,
  withRouting
)(config, App)