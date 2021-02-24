/* eslint-disable */
import "./SystemList.css";
import React, { useState, useEffect } from "react";
import TextField from "@material-ui/core/TextField";
import Select from "@govuk-react/select";
import SystemCard from "../SystemCard/SystemCard";
import Link from "../Linking/Link";

//todo make stateless, all components stateless all, state should be in the containers only
const SystemList = (props) => {
  const systems = props.register?.systems;
  const [visibleSystems, setVisibleSystems] = useState(systems);
  const [selectedPortfolio, setSelectedPortfolio] = useState("all");
  const [searchString, setSearchString] = useState("");
  const portfolios = [...new Set(systems?.map((s) => s.portfolio))].sort();

  const filter = () => {
    setVisibleSystems(
      systems
        ?.filter((s) => matchesPortfolio(s, selectedPortfolio))
        .filter((s) => matchesSearch(s, searchString))
    );
  };

  useEffect(filter, [searchString, selectedPortfolio, props.register]);

  return (
    <div className="centerContent">
      <h1>System search</h1>
      <div className="system-list-filter">
        <span className="system-list-search">
          <TextField
            id="search"
            label="search"
            onChange={(e) => setSearchString(e.target.value)}
          />
        </span>
        <Select
          data-testid="select-portfolio"
          className="system-list-dropdown"
          name="portfolio-select"
          label="Filter by Portfolio"
          onChange={(e) => setSelectedPortfolio(e.target.value)}
          value={selectedPortfolio}
        >
          <option value="all">All Portfolios</option>
          {portfolios.map((p, i) => (
            <option data-testid={`portfolio-option-${p}`} key={i} value={p}>
              {p}
            </option>
          ))}
        </Select>
      </div>
      <div className="topMarginBig">
        <strong>Number of systems identified: </strong>
        {systems?.length}
      </div>
      <p>
        Can't find your system? <Link to={`/add-system`}>Add a system</Link>
      </p>
      <div className="topMarginBig">
        {visibleSystems?.map((system, key) => (
          <SystemCard key={key} system={system} />
        ))}
      </div>
    </div>
  );
};

function containsCaseInsensitive(a, b) {
  return a.toLowerCase().includes(b.toLowerCase());
}

function matchesPortfolio(system, portfolio) {
  return portfolio === "all" || system.portfolio === portfolio;
}

function matchesSearch(system, needle) {
  const result =
    containsCaseInsensitive(system.name, needle) ||
    system.aliases.some((alias) => containsCaseInsensitive(alias, needle));
  return result;
}

export default SystemList;
