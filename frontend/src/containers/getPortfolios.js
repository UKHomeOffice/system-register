import {isEmpty} from "lodash-es";

function getPortfolios(systems){
  if(isEmpty(systems)){
    return []
  } else {
    return [systems[0].portfolio];
  }
}

export default getPortfolios;
