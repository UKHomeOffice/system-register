import React, { useEffect, useState } from 'react';
import { Route, Switch, useRouteMatch } from 'react-router-dom';

import SystemView from "./SystemView/SystemView";
import UpdateContacts from "./UpdateContacts";
import api from '../../services/api';
import './System.css';

function System() {
  const { path, params: { id }} = useRouteMatch();
  const [system, setSystem] = useState(null);

  useEffect(() => {
    const fetchData = async () => setSystem(await api.getSystem(id));
    fetchData();
  }, [id]);

  return (
    <Switch>
      <Route path={`${path}`} exact>
        <SystemView system={system} />
      </Route>
      <Route path={`${path}/update-contacts`}>
        <UpdateContacts system={system} />
      </Route>
    </Switch>
  );
}

export default System;
