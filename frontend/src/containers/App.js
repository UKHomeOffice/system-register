import React from "react";
import { flow } from "lodash-es";

import SystemRegister from "./SystemRegister/SystemRegister";
import withKeycloak from "./withKeycloak";
import withRouting from "./withRouting";
import config from "../config/config";

import "./App.css";

const App = () => <SystemRegister />;

export default flow(withKeycloak, withRouting)(config, App);
