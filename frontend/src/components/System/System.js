import React, {useEffect, useState} from 'react'
import {useParams} from "react-router-dom";
import './System.css'
import api from '../../services/api';
import SystemView from "./SystemView/SystemView";

const System = () => {
  let {id} = useParams();
  let [system, setSystem] = useState(null);

  useEffect(() => {
      const fetchData = async () => setSystem(await api.getSystem(id));
      fetchData()
    },
    [id]);

  return (<SystemView system={system}/>)
};

export default System
