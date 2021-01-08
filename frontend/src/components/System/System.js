import React, { useCallback, useEffect, useState } from 'react';
import { Route, Switch, useHistory, useRouteMatch } from 'react-router-dom';

import SecureRoute from "../SecureRoute";
import SystemView from "./SystemView/SystemView";
import UpdateContacts from "./UpdateContacts";
import api from '../../services/api';
import './System.css';
import UpdateAbout from './UpdateAbout/UpdateAbout';
import UpdateInfo from './UpdateInfo';

function System(props) {
  const { dirtyCallback } = props;
  const { path, url, params: { id } } = useRouteMatch();
  const [system, setSystem] = useState(null);
  const history = useHistory();

  useEffect(() => {
    const fetchData = async () => setSystem(await api.getSystem(id));
    fetchData();
  }, [id]);

  const updateSystem = useCallback((newSysData) => {
    setSystem(newSysData)
    dirtyCallback()
  }, [dirtyCallback])

  const handleUpdateContacts = useCallback(async (data) => {
    if ("productOwner" in data) {
      updateSystem(await api.updateProductOwner(id, data));
    }
    history.push(url);
  }, [id, history, url, updateSystem]);

  const handleUpdateAbout = useCallback(async (data) => {
    updateSystem(await api.updateCriticality(id, data));
    history.push(url);
  }, [id, history, url, updateSystem]);

  const handleUpdateInfo = useCallback(async (data) => {
    if ("name" in data) {
      updateSystem(await api.updateSystemName(id, data));
    }
    if ("description" in data) {
      updateSystem(await api.updateSystemDescription(id, data));
    }
    history.push(url);
  }, [id, history, url, updateSystem]);

  const handleCancel = useCallback(() => {
    history.push(url);
  }, [history, url]);

  return (
    <Switch>
      <Route path={`${path}`} exact>
        <SystemView system={system} />
      </Route>
      <SecureRoute path={`${path}/update-info`}>
        <UpdateInfo system={system} onSubmit={handleUpdateInfo} onCancel={handleCancel} executeCheck={props.executeCheck} withDescription />
      </SecureRoute>
      <SecureRoute path={`${path}/update-about`}>
        <UpdateAbout system={system} onSubmit={handleUpdateAbout} onCancel={handleCancel} />
      </SecureRoute>
      <SecureRoute path={`${path}/update-contacts`}>
        <UpdateContacts system={system} onSubmit={handleUpdateContacts} onCancel={handleCancel} />
      </SecureRoute>
    </Switch>
  );
}

export default System;
