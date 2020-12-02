import axios from 'axios'
import config from '../config/config'

const api = {
    getAllSystems,
    getSystem,
};

async function getAllSystems() {
    // const response = await axios.get(`${config.api.url}/systems`);
    return {
        "systems": [
            {
                "aliases": [
                    "systems register",
                    "systems audit",
                    "system audit"
                ],
                "businessOwner": null,
                "criticality": "low",
                "description": "Central source of system names, contacts and risk information",
                "developedBy": "Home Office",
                "informationAssetOwner": null,
                "investmentState": "evergreen",
                "name": "System Register",
                "portfolio": "SPAN-R",
                "productOwner": null,
                "risks": [
                    {
                        "level": "low",
                        "name": "change",
                        "rationale": "Designed to be easy to change"
                    }
                ],
                "serviceOwner": "Home Office",
                "supportedBy": "All",
                "technicalOwner": "Home Office",
                "id": 1,
                "last_updated": {
                    "timestamp": "",
                    "author_name": ""
                }
            },
            {
                "aliases": [
                    "testing more entries"
                ],
                "businessOwner": null,
                "criticality": "high",
                "description": "I'm a cute new system",
                "developedBy": "Home Office",
                "informationAssetOwner": null,
                "investmentState": "sunset",
                "name": "Our second system",
                "portfolio": "SPAN-R",
                "productOwner": null,
                "risks": [
                    {
                        "level": "medium",
                        "name": "scorpion",
                        "rationale": "Designed to sting"
                    }
                ],
                "serviceOwner": "Home Office",
                "supportedBy": "All",
                "technicalOwner": "Fun Office",
                "id": 2,
                "last_updated": {
                    "timestamp": "2020-11-18T11:22:50.326570Z",
                    "author_name": "Betty Franklin"
                }
            }
        ],
        "timestamp": "2020-10-09T16:04:36.227867Z"
    };
}

async function getSystem(id) {
    const register = await getAllSystems();
    return register.systems.find(s => s.id.toString() === id);
}


export default api