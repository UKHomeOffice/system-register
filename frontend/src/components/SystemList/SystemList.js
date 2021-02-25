import React, { useState } from "react";
import PropTypes from "prop-types";
import { InputField, Select } from "govuk-react";
import { flow, map, partialRight, sortBy, sortedUniq } from "lodash-es";

import Link from "../Linking/Link";
import SystemCard from "../SystemCard/SystemCard";

import "./SystemList.css";

const mapToPortfolio = partialRight(map, "portfolio");
const getPortfolios = flow(mapToPortfolio, sortBy, sortedUniq);

function SystemList({ register }) {
  const systems = register.systems || [];
  const [selectedPortfolio, setSelectedPortfolio] = useState("all");
  const [searchString, setSearchString] = useState("");
  const portfolios = getPortfolios(systems);

  const visibleSystems = systems.filter(
    (system) =>
      matchesPortfolio(system, selectedPortfolio) &&
      matchesSearch(system, searchString)
  );

  return (
    <div className="centerContent">
      <h1>System search</h1>
      <div className="system-list-filter">
        <InputField
          input={{ placeholder: "Search..." }}
          onChange={(e) => setSearchString(e.target.value)}
        >
          <span className="system-list--visually-hidden">
            Find a system on the System Register
          </span>
        </InputField>

        <Select
          className="system-list-dropdown"
          name="portfolio-select"
          label="Filter by Portfolio"
          onChange={(e) => setSelectedPortfolio(e.target.value)}
          value={selectedPortfolio}
        >
          <option value="all">All Portfolios</option>
          {portfolios.map((portfolio, index) => (
            <option key={index} value={portfolio}>
              {portfolio}
            </option>
          ))}
        </Select>
      </div>

      <div className="topMarginBig">
        <strong>Number of systems identified: </strong>
        {visibleSystems.length}
      </div>
      <p>
        Can’t find your system? <Link to={`/add-system`}>Add a system</Link>
      </p>

      <div className="topMarginBig">
        {visibleSystems.map((system, key) => (
          <SystemCard key={key} system={system} />
        ))}
      </div>
    </div>
  );
}

SystemList.propTypes = {
  register: PropTypes.shape({
    systems: PropTypes.arrayOf(
      PropTypes.shape({
        name: PropTypes.string.isRequired,
        aliases: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
        portfolio: PropTypes.string,
      }).isRequired
    ).isRequired,
  }),
};

function containsCaseInsensitive(a, b) {
  return a.toLowerCase().includes(b.toLowerCase());
}

function matchesPortfolio(system, portfolio) {
  return portfolio === "all" || system.portfolio === portfolio;
}

function matchesSearch(system, needle) {
  return (
    containsCaseInsensitive(system.name, needle) ||
    system.aliases.some((alias) => containsCaseInsensitive(alias, needle))
  );
}

export default SystemList;
