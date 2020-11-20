import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import riskConfig from '../../utilities/riskConfiguration'
import Criticality from '../System/Criticality/Criticality'
import './RiskList.css'
import toTitle from '../../utilities/toTitle'
import { Link } from 'react-router-dom'
import { sumKnownRisk } from '../../utilities/riskCalculator';
import { styleRiskBackground, styleRiskScoreBackground } from '../../utilities/styleRisk';
import scoreCriticality from '../../utilities/scoreCriticality';
import toUpper from "../../utilities/toUpper";

const useStyles = makeStyles({
    head: {
        backgroundColor: 'black',
        color: 'white',
    },
    table: {
        maxWidth: 500,
    },
    container: {
        minHeight: 650,
        maxHeight: 650,
    }
});

const ALL_RISK_LENSE = 'combined'

const RiskList = (props) => {
    const classes = useStyles();

    let systems = []

    if (props.riskLens === ALL_RISK_LENSE) {
        systems = props.systems.map(sys => {
            const risk = { name: ALL_RISK_LENSE, score: sumKnownRisk(sys) }
            return { name: sys.name, id: sys.id, criticality: sys.criticality, risk: risk }
        }).sort((a, b) => b.risk.score - a.risk.score)
    }
    else {
        systems = props.systems.map(sys => {
            const risk = sys.risks.find(r => r.name === props.riskLens)
            risk.score = riskConfig.mapToKnownRisk(risk.level)
            return { name: sys.name, id: sys.id, criticality: sys.criticality, risk: risk }
        }).sort((a, b) => b.risk.score - a.risk.score
            || scoreCriticality(b.criticality) - scoreCriticality(a.criticality))
        //todo test ordering
    }
    const riskTypes = props.systems[0]?.risks

    return (
        <div className="riskList">
            <TableContainer component={Paper} className={classes.container}>
                <Table stickyHeader className={classes.table} size="small" aria-label="a dense table">
                    <TableHead>
                        <TableRow>
                            <TableCell className={classes.head} align="left"><strong>System</strong></TableCell>
                            <TableCell className={classes.head} align="left"><strong>Criticality</strong></TableCell>
                            <TableCell className={classes.head} align="center"><strong>{toTitle(props.riskLens)} Risk</strong></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {systems.map((s) => {
                            const riskClass = props.riskLens === ALL_RISK_LENSE ?
                                styleRiskScoreBackground(0, riskTypes.length * riskConfig.RISK_VALUES.high, s.risk.score)
                                : styleRiskBackground(s.risk.level)
                            return (
                                <TableRow className={classes.row} key={s.id}>
                                    <TableCell align="left" scope="row">
                                        <Link to={`system/${s.id}`}>{s.name}</Link>
                                    </TableCell>
                                    <TableCell align="left"><Criticality level={s.criticality} hideLabel /></TableCell>
                                    <TableCell data-testid={`risk_cell_${s.name}`} className={riskClass} align="left">
                                        <strong style={{ color: 'white' }}>
                                            {props.riskLens === ALL_RISK_LENSE ?
                                                s.risk.score
                                                : formatLevel(s.risk.level, props.rootValue)}
                                        </strong>
                                    </TableCell>
                                </TableRow>
                            )
                        }
                        )}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
}

// //TODO: Re use the following 
const NOT_APPLICABLE = 'not_applicable'
function formatLevel(level) {
    if (!level) return "UKNOWN"
    if (level === NOT_APPLICABLE) {
        return 'N/A'
    }
    return toUpper(level)
}

export default RiskList
