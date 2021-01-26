import React, { useCallback, useEffect, useState } from 'react';
import { Route, Switch, useHistory, useRouteMatch } from 'react-router-dom';
import { filter, isEmpty, map } from "lodash-es";

import PageNotFoundError from '../../components/Errors/PageNotFoundError';
import SecureRoute from "../SecureRoute";
import SystemView from "./SystemView/SystemView";
import UpdateAbout from './UpdateAbout/UpdateAbout';
import UpdateContacts from "./UpdateContacts";
import UpdateInfo from './UpdateInfo';
import api from '../../services/api';
import useAsyncError from '../../utilities/useAsyncError';

import './System.css';

const actionsByField = {
  name: api.updateSystemName,
  description: api.updateSystemDescription,
  criticality: api.updateCriticality,
  investmentState: api.updateInvestmentState,
  portfolio: api.updatePortfolio,
  businessOwner: api.updateBusinessOwner,
  productOwner: api.updateProductOwner,
  technicalOwner: api.updateTechnicalOwner,
  serviceOwner: api.updateServiceOwner,
  informationAssetOwner: api.updateInformationAssetOwner,
};

const findMatchingActions = (data, fields) => map(
  filter(fields, (field) => field in data),
  (field) => actionsByField[field]
);

function useReturnToSystemView() {
  const history = useHistory();
  const { url } = useRouteMatch();

  return useCallback(() => {
    history.push(url);
  }, [history, url]);
}

function useUpdateCallbackFactory(id, onChange, setSystem) {
  const returnToSystemView = useReturnToSystemView();

  return useCallback((...fields) => async (data) => {
    const actions = findMatchingActions(data, fields);
    for (const action of actions) {
      setSystem(await action(id, data));
    }
    if (!isEmpty(actions)) {
      onChange();
    }
    returnToSystemView();
  }, [id, returnToSystemView, onChange, setSystem]);
}

function System({ portfolios, onChange, onBeforeNameChange }) {
  const { path, params: { id } } = useRouteMatch();
  const [system, setSystem] = useState(null);
  const throwError = useAsyncError();

  useEffect(() => {
    const fetchData = async () => setSystem(await api.getSystem(id));
    fetchData().catch((e) => { throwError(e) });
  }, [id, throwError]);

  const createUpdateCallback = useUpdateCallbackFactory(id, onChange, setSystem);
  const handleUpdateInfo = createUpdateCallback("name", "description");
  const handleUpdateAbout = createUpdateCallback("portfolio", "criticality", "investmentState");
  const handleUpdateContacts = createUpdateCallback("businessOwner", "technicalOwner", "serviceOwner", "productOwner", "informationAssetOwner");

  const handleCancel = useReturnToSystemView();

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
