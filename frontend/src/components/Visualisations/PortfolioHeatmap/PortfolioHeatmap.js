import React, { useState, useEffect } from 'react'
import './PortfolioHeatmap.css'
import FormControl from '@material-ui/core/FormControl'
import FormControlLabel from '@material-ui/core/FormControlLabel'
import RadioGroup from '@material-ui/core/RadioGroup'
import Radio from '@material-ui/core/Radio'
import SystemsHeatmapVis from './SystemsHeatmap'
import RiskList from '../../RiskList/RiskList'
import RiskSummary from '../../RiskSummary/RiskSummary'
import { Tooltip } from '@material-ui/core'
import toTitle from '../../../utilities/toTitle'
import api from '../../../services/api'

//TODO mock api and remove props parameter from <PortfolioHeatmap /> component

const PortfolioHeatmap = (props) => {
    const HO_ROOT = ''
    const PORTFOLIO_ROOT = 'portfolio'
    let [systems, setSystems] = useState(props.systems)
    useEffect(() => {
        const fetchData = async () => {
            const sys = await api.getAllSystems()
            setSystems(sys.systems)
        }
        fetchData()
    }, []);
    const [selectedView, setView] = useState('knownRisk')
    const [selectedCell, setSelectedCell] = useState(
        { rootType: HO_ROOT, rootValue: 'Home Office', riskLens: 'combined', systems: systems }
    )

    const handleViewChange = (event) => setView(event.target.value)

    const cellSelected = (event) => {
        setSelectedCell(
            {
                rootType: PORTFOLIO_ROOT,
                rootValue: event.rootValue,
                riskLens: event.riskLens,
                systems: systems?.filter(s => s[PORTFOLIO_ROOT] === event.rootValue)
            }
        )
    }

    const knownRiskInfo = `Display the scores of all the systems that have been audited.
    Red = higher risk, green = lower risk.`
    const includeUnknownRiskInfo = `Display scores that treat unknown risk
    (including systems that have not been audited) as inherantly risky.
    Red = higher risk, green = lower risk.`
    const unknownsInfo = `Display the proportion of known systems have been audited against each portfolio risk lense.
    Blue = more coverage, White = less coverage.`

    let vis = <p>Loading visualisation...</p>
    if (systems?.length > 0) {
        vis = <>
            <div className="centerContent">
                <h2>Aggregated risk by portfolio</h2>
                <p>
                    The following visulisation shows an aggregated view of risk across each portfolio.
                    Risk is broken down by different "lenses" such as roadmap, technology or commercial.
                    Each system is individualy scored against each of these lenses,
                    and the scores of all systems within a portfolio are summed up to provide the overall scores visible below.
  </p>
                <p>
                    These scores are normalised against the number of systems within the portfolio,
                    so portfolios with many systems can be compared more easily against portfolios with fewer.
  </p>
            </div>
            <div className="visContainer">
                <div className="leftVis">
                    <SystemsHeatmapVis systems={systems} view={selectedView} root={PORTFOLIO_ROOT} panelHeight="40" cellSelectedCallback={cellSelected} />
                    <FormControl component="fieldset">
                        <RadioGroup name="view" value={selectedView} onChange={handleViewChange} row>
                            <Tooltip title={knownRiskInfo}>
                                <FormControlLabel data-testid="radioKnownRisk" value="knownRisk" control={<Radio />} label="Known risk only" />
                            </Tooltip>
                            <Tooltip title={includeUnknownRiskInfo}>
                                <FormControlLabel data-testid="radioUnknownRisk" value="includeUnknownRisk" control={<Radio />} label="Include unknown risk" />
                            </Tooltip>
                            <Tooltip title={unknownsInfo}>
                                <FormControlLabel data-testid="radioCoverage" value="unknowns" control={<Radio />} label="Audit coverage" />
                            </Tooltip>
                        </RadioGroup>
                    </FormControl>
                    <RiskSummary systems={selectedCell.systems} riskLens={selectedCell.riskLens} root={selectedCell.rootValue} />
                </div>
                <div className="rightVis">
                    <h3>{toTitle(selectedCell.riskLens)} risk to systems in {selectedCell.rootValue} {selectedCell.rootType}</h3>
                    <RiskList
                        systems={selectedCell.systems}
                        riskLens={selectedCell.riskLens}
                        rootType={selectedCell.rootType}
                        rootValue={selectedCell.rootValue} />
                </div>
            </div>
        </>
    }

    return (
        <div className="portfolioHeatmapContainer">
            {vis}
        </div>
    )
}

export default PortfolioHeatmap
