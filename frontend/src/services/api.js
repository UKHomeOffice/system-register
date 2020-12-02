import axios from 'axios'
import config from '../config/config'

const api = {
    getAllSystems,
    getSystem,
};

async function getAllSystems() {
    const response = await axios.get(`${config.api.url}/systems`);
    return response.data;
}

async function getSystem(id) {
    const register = await getAllSystems();
    return register.systems.find(s => s.id.toString() === id);
}


export default api