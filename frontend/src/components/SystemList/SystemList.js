/* eslint-disable */
import './SystemList.css'
import React, { useState, useEffect } from 'react'
import TextField from '@material-ui/core/TextField'
import MenuItem from "@material-ui/core/MenuItem";
import Select from "@material-ui/core/Select";
import SystemCard from "../SystemCard/SystemCard"

//todo make stateless, all components stateless all, state should be in the containers only
const SystemList = props => {
    const systems = props.register.systems
    const [visibleSystems, setVisibleSystems] = useState(systems)
    const [selectedPortfolio, setSelectedPortfolio] = useState('all')
    const [searchString, setSearchString] = useState('')
    const portfolios = [...new Set(systems?.map(s => s.portfolio))].sort()

    const filter = () => {
        setVisibleSystems(
            systems?.filter((s) => matchesPortfolio(s, selectedPortfolio))
                    .filter((s) => matchesSearch(s, searchString))
        )
    }

    useEffect(filter, [searchString, selectedPortfolio, props.register])

    return (
        <div className="centerContent">
            <div className="topMarginBig">
                <strong>Number of systems identified: </strong>{systems?.length}
            </div>
            <div className="bottomMargin topMarginBig">
                <TextField
                    id="search"
                    label="search"
                    onChange={e => setSearchString(e.target.value)}
                />
                <Select
                    value={selectedPortfolio}
                    onChange={e => setSelectedPortfolio(e.target.value)}
                    className="dropdown">
                    <MenuItem value="all">
                        All Portfolios
                    </MenuItem>
                    {portfolios.map((p, i) => <MenuItem key={i} value={p}>{p}</MenuItem>)}
                </Select>
            </div>
            {/* todo refactor out to functional <Systems /> component */}
            <div className="topMarginBig">
                {
                    visibleSystems?.map((system, key) => <SystemCard key={key} system={system} />)
                    /* todo use id rather than key to help out the shadow DOM */
                }
            </div>
        </div >
    )
}

function containsCaseInsensitive(a, b) {
    return a.toLowerCase().includes(b.toLowerCase())
}

function matchesPortfolio(system, portfolio) {
    return portfolio === "all" || system.portfolio === portfolio
}

function matchesSearch(system, needle) {
    const result = containsCaseInsensitive(system.name, needle) ||
        system.aliases.some((alias) => containsCaseInsensitive(alias, needle))
    return result
}

export default SystemList
