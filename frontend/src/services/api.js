import axios from 'axios'
import config from '../config/config'

const api = {
    getAllSystems: async () => await getAllSystems(),
    getSystem : async (id) => {
        const register = await getAllSystems()
        return register.systems.find(s => s.id.toString() === id)
    }
}

export default api

async function getAllSystems() {
    const register = localStorage.getItem('register');
    if (register === null) {
        const response = await axios.get(`${config.api.url}/systems`);
        localStorage.setItem('register', JSON.stringify(response.data));
        return response.data
    }
    return JSON.parse(register);
}
