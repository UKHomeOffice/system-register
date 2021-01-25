import axios from 'axios'
import config from '../config/config'
import SystemNotFoundException from './systemNotFoundException';
import ValidationError from "./validationError";

const api = {
  getAllSystems,
  getSystem,
  updateSystemName,
  updateSystemDescription,
  updatePortfolio,
  updateCriticality,
  updateInvestmentState,
  updateProductOwner,
  updateTechnicalOwner,
  updateInformationAssetOwner,
  updateBusinessOwner,
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
  const matchingSystem = register.systems.find(s => s.id.toString() === id);
  if (matchingSystem) { return matchingSystem }
  else { throw new SystemNotFoundException(); }
}

async function updateProductOwner(id, data) {
  const response = await sendPost(`systems/${id}/update-product-owner`, {
    product_owner: nullIfEmpty(data.productOwner),
  });
  return response.data;
}

async function updateTechnicalOwner(id, data) {
  const response = await sendPost(`systems/${id}/update-technical-owner`, {
    technical_owner: nullIfEmpty(data.technicalOwner),
  });
  return response.data;
}

async function updateBusinessOwner(id, data) {
  const response = await sendPost(`systems/${id}/update-business-owner`, {
    business_owner: nullIfEmpty(data.businessOwner),
  });
  return response.data;
}

async function updateInformationAssetOwner(id, data) {
  const response = await sendPost(`systems/${id}/update-information-asset-owner`, {
    information_asset_owner: nullIfEmpty(data.informationAssetOwner),
  });
  return response.data;
}

async function updateSystemName(id, data) {
  const response = await sendPost(`systems/${id}/update-name`, {
    name: nullIfEmpty(data.name),
  });
  return response.data;
}

async function updateSystemDescription(id, data) {
  const response = await sendPost(`systems/${id}/update-system-description`, {
    description: nullIfEmpty(data.description),
  });
  return response.data;
}

async function updatePortfolio(id, data) {
  const response = await sendPost(`systems/${id}/update-portfolio`, {
    portfolio: nullIfEmpty(data.portfolio),
  });
  return response.data;
}

async function updateCriticality(id, data) {
  const response = await sendPost(`systems/${id}/update-criticality`, {
    criticality: nullIfEmpty(data.criticality),
  });
  return response.data;
}

async function updateInvestmentState(id, data) {
  const response = await sendPost(`systems/${id}/update-investment-state`, {
    investment_state: nullIfEmpty(data.investmentState),
  });
  return response.data;
}

async function sendPost(path, data) {
  const response = await axios.post(
    `${config.api.url}/${path}`,
    data,
    {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("bearer-token")}`,
      },
      validateStatus: status => status < 500,
    }
  );
  switch (response.status) {
    case 400:
      throw new ValidationError(response.data.errors);
    case 404:
      throw new SystemNotFoundException();
    default:
      return response;
  }
}

export default api;
