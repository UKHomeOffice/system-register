import axios from 'axios'
import config from '../config/config'

const api = {
  getAllSystems,
  getSystem,
  updateProductOwner,
  updateCriticality
};

const nullIfEmpty = (value) => value !== "" ? value : null;

async function getAllSystems() {
  const response = await axios.get(`${config.api.url}/systems`);
  const sortedSystems = response.data.systems.sort((a, b) => {
    const x = a.name.toUpperCase();
    const y = b.name.toUpperCase();
    if (x > y) {
      return 1
    } else if (x < y) {
      return -1
    }
    return 0
  })
  return { ...response.data, systems: sortedSystems };
}

async function getSystem(id) {
  const register = await getAllSystems();
  return register.systems.find(s => s.id.toString() === id);
}

async function updateProductOwner(id, data) {
  const response = await axios.post(
    `${config.api.url}/systems/${id}/update-product-owner`,
    { product_owner: nullIfEmpty(data.productOwner) },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("bearer-token")}`,
      }
    });
  return response.data;
}

async function updateCriticality(id, data) {
  const response = await axios.post(
    `${config.api.url}/systems/${id}/update-criticality`,
    { criticality: nullIfEmpty(data.criticality) },
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("bearer-token")}`,
      }
    });
  return response.data;
}

export default api;
