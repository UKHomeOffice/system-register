import { isEmpty, isEqual } from "lodash-es";

function getPortfolios(systems) {
  if (isEmpty(systems)) {
    return [];
  }
  return [...new Set(
    systems
      .filter(system => !isEmpty(system.portfolio) && !isEqual(system.portfolio, "Unknown"))
      .map(system => system.portfolio.trim()))];
}

export default getPortfolios;
