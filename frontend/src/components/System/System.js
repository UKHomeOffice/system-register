import React, { useEffect, useState } from 'react';
import { Route, Switch, useParams } from 'react-router-dom';

import SystemView from "./SystemView/SystemView";
import api from '../../services/api';
import './System.css';

function System() {
  const { id } = useParams();
  const [system, setSystem] = useState(null);

  useEffect(() => {
    const fetchData = async () => setSystem(await api.getSystem(id));
    fetchData();
  }, [id]);

  return (
    <Switch>
      <Route path="" exact>
        <SystemView system={system} />
      </Route>
    </Switch>
  );
}

export default System;
