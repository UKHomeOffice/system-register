import React, { useCallback, useEffect, useState } from 'react';
import { Route, Switch, useHistory, useRouteMatch } from 'react-router-dom';

import SecureRoute from "../SecureRoute";
import SystemView from "./SystemView/SystemView";
import UpdateContacts from "./UpdateContacts";
import api from '../../services/api';
import './System.css';
import UpdateAbout from './UpdateAbout/UpdateAbout';

function System() {
  const { path, url, params: { id } } = useRouteMatch();
  const [system, setSystem] = useState(null);
  const history = useHistory();

  useEffect(() => {
    const fetchData = async () => setSystem(await api.getSystem(id));
    fetchData();
  }, [id]);

  const handleUpdateContacts = useCallback(async (data) => {
    if ("productOwner" in data) {
      setSystem(await api.updateProductOwner(id, data));
    }
    history.push(url);
  }, [id, history, url]);

  const handleUpdateAbout = useCallback(async (data) => {
    setSystem(await api.updateCriticality(id, data));
    history.push(url);
  }, [id, history, url]);

  const handleCancel = useCallback(() => {
    history.push(url);
  }, [history, url]);

  return (
    <Switch>
      <Route path={`${path}`} exact>
        <SystemView system={system} />
      </Route>
      <SecureRoute path={`${path}/update-contacts`}>
        <UpdateContacts system={system} onSubmit={handleUpdateContacts} onCancel={handleCancel} />
      </SecureRoute>
      <SecureRoute path={`${path}/update-about`}>
        <UpdateAbout system={system} onSubmit={handleUpdateAbout} onCancel={handleCancel} />
      </SecureRoute>
    </Switch>
  );
}

export default System;
