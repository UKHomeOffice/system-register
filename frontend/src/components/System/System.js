import React, { useCallback, useEffect, useState } from 'react';
import { Route, Switch, useHistory, useRouteMatch } from 'react-router-dom';

import SecureRoute from "../SecureRoute";
import SystemView from "./SystemView/SystemView";
import UpdateContacts from "./UpdateContacts";
import api from '../../services/api';
import './System.css';
import UpdateAbout from './UpdateAbout/UpdateAbout';
import UpdateInfo from './UpdateInfo';
import useAsyncError from '../../utilities/useAsyncError';
import PageNotFoundError from '../../components/Errors/PageNotFoundError';

function System({ portfolios, onChange, onBeforeNameChange }) {
  const { path, url, params: { id } } = useRouteMatch();
  const [system, setSystem] = useState(null);
  const history = useHistory();
  const throwError = useAsyncError();

  useEffect(() => {
    const fetchData = async () => setSystem(await api.getSystem(id));
    fetchData().catch((e) => { throwError(e) });
  }, [id, throwError]);

  const updateSystem = useCallback((newSysData) => {
    setSystem(newSysData);
    onChange();
  }, [onChange])

  const handleUpdateContacts = useCallback(async (data) => {
    if ("productOwner" in data) {
      updateSystem(await api.updateProductOwner(id, data));
    }
    history.push(url);
  }, [id, history, url, updateSystem]);

  const handleUpdateAbout = useCallback(async (data) => {
    if ("portfolio" in data) {
      updateSystem(await api.updatePortfolio(id, data));
    }
    if ("criticality" in data) {
      updateSystem(await api.updateCriticality(id, data));
    }
    if ("investmentState" in data) {
      updateSystem(await api.updateInvestmentState(id, data));
    }
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
        <UpdateInfo system={system} onSubmit={handleUpdateInfo} onCancel={handleCancel} onBeforeNameChange={onBeforeNameChange} />
      </SecureRoute>
      <SecureRoute path={`${path}/update-about`}>
        <UpdateAbout system={system} portfolios={portfolios} onSubmit={handleUpdateAbout} onCancel={handleCancel} />
      </SecureRoute>
      <SecureRoute path={`${path}/update-contacts`}>
        <UpdateContacts system={system} onSubmit={handleUpdateContacts} onCancel={handleCancel} />
      </SecureRoute>
      <Route path="/*" component={PageNotFoundError} />
    </Switch>
  );
}

export default System;
