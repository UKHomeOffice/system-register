import React from "react";
import PropTypes from "prop-types";
import { makeStyles } from "@material-ui/core/styles";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@material-ui/core";
import { Link } from "react-router-dom";

import Criticality from "../System/Criticality/Criticality";
import riskConfig from "../../utilities/riskConfiguration";
import scoreCriticality from "../../utilities/scoreCriticality";
import toTitle from "../../utilities/toTitle";
import toUpper from "../../utilities/toUpper";
import { sumKnownRisk } from "../../utilities/riskCalculator";
import {
  styleRiskBackground,
  styleRiskScoreBackground,
} from "../../utilities/styleRisk";

import "./RiskList.css";

const useStyles = makeStyles({
  head: {
    backgroundColor: "black",
    color: "white",
  },
  table: {
    maxWidth: 500,
  },
  container: {
    minHeight: 650,
    maxHeight: 650,
  },
});

const ALL_RISK_LENSES = "combined";

const RiskList = (props) => {
  const classes = useStyles();

  let systems;
  if (props.riskLens === ALL_RISK_LENSES) {
    systems = props.systems
      .map((sys) => {
        const risk = { name: ALL_RISK_LENSES, score: sumKnownRisk(sys) };
        return {
          name: sys.name,
          id: sys.id,
          criticality: sys.criticality,
          risk: risk,
        };
      })
      .sort((a, b) => b.risk.score - a.risk.score);
  } else {
    systems = props.systems
      .map((sys) => {
        const risk = sys.risks.find((r) => r.name === props.riskLens);
        risk.score = riskConfig.mapToKnownRisk(risk.level);
        return {
          name: sys.name,
          id: sys.id,
          criticality: sys.criticality,
          risk: risk,
        };
      })
      .sort(
        (a, b) =>
          b.risk.score - a.risk.score ||
          scoreCriticality(b.criticality) - scoreCriticality(a.criticality)
      );
    //todo test ordering
  }
  const riskTypes = props.systems[0]?.risks;

  return (
    <div className="riskList">
      <TableContainer component={Paper} className={classes.container}>
        <Table
          stickyHeader
          className={classes.table}
          size="small"
          aria-label="a dense table"
        >
          <TableHead>
            <TableRow>
              <TableCell className={classes.head} align="left">
                <strong>System</strong>
              </TableCell>
              <TableCell className={classes.head} align="left">
                <strong>Criticality</strong>
              </TableCell>
              <TableCell className={classes.head} align="center">
                <strong>{toTitle(props.riskLens)} Risk</strong>
              </TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {systems.map((s) => {
              const riskClass =
                props.riskLens === ALL_RISK_LENSES
                  ? styleRiskScoreBackground(
                      0,
                      riskTypes.length * riskConfig.RISK_VALUES.high,
                      s.risk.score
                    )
                  : styleRiskBackground(s.risk.level);
              return (
                <TableRow className={classes.row} key={s.id}>
                  <TableCell align="left" scope="row">
                    <Link to={`system/${s.id}`}>{s.name}</Link>
                  </TableCell>
                  <TableCell align="left">
                    <Criticality level={s.criticality} hideLabel />
                  </TableCell>
                  <TableCell
                    data-testid={`risk_cell_${s.name}`}
                    className={riskClass}
                    align="left"
                  >
                    <strong style={{ color: "white" }}>
                      {props.riskLens === ALL_RISK_LENSES
                        ? s.risk.score
                        : formatLevel(s.risk.level, props.rootValue)}
                    </strong>
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
};

RiskList.propTypes = {
  riskLens: PropTypes.string.isRequired,
  rootValue: PropTypes.string,
  systems: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      name: PropTypes.string.isRequired,
      criticality: PropTypes.string,
      risks: PropTypes.arrayOf(
        PropTypes.shape({
          name: PropTypes.string.isRequired,
          level: PropTypes.string,
        }).isRequired
      ).isRequired,
    }).isRequired
  ).isRequired,
};

// TODO: Re use the following
const NOT_APPLICABLE = "not_applicable";

function formatLevel(level) {
  if (!level) return "UNKNOWN";
  if (level === NOT_APPLICABLE) {
    return "N/A";
  }
  return toUpper(level);
}

export default RiskList;
