import React, { useState, useEffect } from "react";
import {
  FormControl,
  FormControlLabel,
  RadioGroup,
  Radio,
  Tooltip,
} from "@material-ui/core";

import PageTitle from "../../PageTitle";
import RiskList from "../../RiskList/RiskList";
import RiskSummary from "../../RiskSummary/RiskSummary";
import SystemsHeatmapVis from "./SystemsHeatmap";
import api from "../../../services/api";
import toTitle from "../../../utilities/toTitle";

import "./PortfolioHeatmap.css";

const PortfolioHeatmap = () => {
  const HO_ROOT = "";
  const PORTFOLIO_ROOT = "portfolio";
  const [systems, setSystems] = useState([]);
  useEffect(() => {
    const fetchData = async () => {
      const sys = await api.getAllSystems();
      setSystems(sys.systems);
      // noinspection JSCheckFunctionSignatures: false positive
      setSelectedCell((prev) => ({ ...prev, systems: sys.systems }));
    };
    // noinspection JSIgnoredPromiseFromCall: call can have async side effect
    fetchData();
  }, []);
  const [selectedView, setView] = useState("knownRisk");
  const [selectedCell, setSelectedCell] = useState({
    rootType: HO_ROOT,
    rootValue: "Home Office",
    riskLens: "combined",
    systems: systems,
  });

  const handleViewChange = (event) => setView(event.target.value);

  const cellSelected = (event) => {
    setSelectedCell({
      rootType: PORTFOLIO_ROOT,
      rootValue: event.rootValue,
      riskLens: event.riskLens,
      systems: systems?.filter((s) => s[PORTFOLIO_ROOT] === event.rootValue),
    });
  };

  const knownRiskInfo = `Display the scores of all the systems that have been audited.
    Red = higher risk, green = lower risk.`;
  const includeUnknownRiskInfo = `Display scores that treat unknown risk
    (including systems that have not been audited) as inherently risky.
    Red = higher risk, green = lower risk.`;
  const unknownsInfo = `Display the proportion of known systems have been audited against each portfolio risk lense.
    Blue = more coverage, White = less coverage.`;

  const vis =
    systems?.length > 0 ? (
      <>
        <div className="centerContent">
          <PageTitle>Risk dashboard</PageTitle>

          <h2>Aggregated risk by portfolio</h2>
          <p>
            The following visualisation shows an aggregated view of risk across
            each portfolio. Risk is broken down by different “lenses” such as
            roadmap, technology or commercial. Each system is individually
            scored against each of these lenses, and the scores of all systems
            within a portfolio are summed up to provide the overall scores
            visible below.
          </p>
          <p>
            These scores are normalised against the number of systems within the
            portfolio, so portfolios with many systems can be compared more
            easily against portfolios with fewer.
          </p>
        </div>
        <div className="visContainer">
          <div className="leftVis">
            <SystemsHeatmapVis
              systems={systems}
              view={selectedView}
              root={PORTFOLIO_ROOT}
              panelHeight="40"
              cellSelectedCallback={cellSelected}
            />
            <FormControl component="fieldset">
              <RadioGroup
                name="view"
                value={selectedView}
                onChange={handleViewChange}
                row
              >
                <Tooltip title={knownRiskInfo}>
                  <FormControlLabel
                    data-testid="radioKnownRisk"
                    value="knownRisk"
                    control={<Radio />}
                    label="Known risk only"
                  />
                </Tooltip>
                <Tooltip title={includeUnknownRiskInfo}>
                  <FormControlLabel
                    data-testid="radioUnknownRisk"
                    value="includeUnknownRisk"
                    control={<Radio />}
                    label="Include unknown risk"
                  />
                </Tooltip>
                <Tooltip title={unknownsInfo}>
                  <FormControlLabel
                    data-testid="radioCoverage"
                    value="unknowns"
                    control={<Radio />}
                    label="Audit coverage"
                  />
                </Tooltip>
              </RadioGroup>
            </FormControl>
            <RiskSummary
              systems={selectedCell.systems}
              riskLens={selectedCell.riskLens}
              root={selectedCell.rootValue}
            />
          </div>
          <div className="rightVis">
            <h3>
              {toTitle(selectedCell.riskLens)} risk to systems in{" "}
              {selectedCell.rootValue} {selectedCell.rootType}
            </h3>
            <RiskList
              systems={selectedCell.systems}
              riskLens={selectedCell.riskLens}
              rootType={selectedCell.rootType}
              rootValue={selectedCell.rootValue}
            />
          </div>
        </div>
      </>
    ) : (
      <div className="centerContent">
        <p>Loading visualisation...</p>
      </div>
    );

  return <div className="centerContent">{vis}</div>;
};

export default PortfolioHeatmap;
