import React, { useEffect } from 'react'
import * as d3 from 'd3'
import riskConfig from '../../../utilities/riskConfiguration'
import toTitle from '../../../utilities/toTitle'
import { summariseRisk } from '../../../utilities/riskCalculator'
import { styleRiskScore } from '../../../utilities/styleRisk'

const PADDING = 3
const WIDTH = 680
const OFFSET_X = 100
const OFFSET_Y = 120

//todo since refactoring out, needs tests can take some tests from PortfolioHeatmap
const SystemsHeatmapVis = (props) => {
    // eslint-disable-next-line
    if(props.systems.length === 0){
        return null
    }
    useEffect(() => renderChart(props.systems, props.panelHeight, props.root, props.cellSelectedCallback), [])
    useEffect(() => switchView(props.view), [props.view])

    return (
        <div className="systemsHeatmap" >
        </div >
    )
}

function switchView(view) {
    d3.selectAll(".riskCell").attr("class", d => calculateRiskClass(d, view) + " riskCell")
}

function renderChart(systems, panelHeight, root, cellSelectedCallback) {
    const data = transformData(systems, root)
    const riskLenses = systems[systems.length -1].risks.map(r => r.name)
    const height = (data.length * panelHeight) + OFFSET_Y
    const panelWidth = (WIDTH - OFFSET_X) / riskLenses.length

    d3.select('.systemsHeatmap')
        .append("svg").attr("width", WIDTH)
        .attr("height", height)
        .attr("id", "vis")

    setupChart(data, panelHeight, OFFSET_X, OFFSET_Y)
    renderYAxis(data, panelHeight, height)
    renderXAxis(riskLenses, panelWidth, WIDTH)
    renderRiskPanels(data, panelWidth, panelHeight, PADDING, cellSelectedCallback)
}

function transformData(systems, root) {
    return d3.nest().key(d => d[root]).rollup(s => summariseRisk(s)).entries(systems)
}

function setupChart(riskData, panelHeight, xOffset, yOffset) {
    d3.select("#vis")
        .append("g")
        .attr("id", "grid")
        .attr("transform", `translate(${xOffset}, ${yOffset})`)
        .selectAll(".rootRow")
        .data(riskData)
        .enter()
        .append("g")
        .attr("class", "rootRow")
        .attr("id", d => d.key)
        .attr("transform", (d, i) => `translate(0, ${(i * panelHeight)})`)
}

function renderRiskPanels(data, panelWidth, panelHeight, panelPadding, cellSelectedCallback) {
    data.forEach(root => {
        d3.select("g[id='" + root.key + "']")
            .selectAll(".riskPanel")
            .data(root.value)
            .enter()
            .append("g")
            .attr("class", "riskPanel")
            .attr("transform", (d, i) => `translate(${(i * panelWidth)}, ${0})`)
            .append("rect")
            .attr("data-testid", d => "riskSquare-" + d.name + "-" + root.key)
            .attr("width", panelWidth - panelPadding)
            .attr("height", panelHeight - panelPadding)
            .attr("class", d => calculateRiskClass(d) + " riskCell")
            .on("click", d => {
              d3.selectAll(".riskCell").classed('selectedCell', false)
              d3.select(d3.event.target).classed("selectedCell", true)
              cellSelectedCallback({ rootValue: root.key, riskLens: d.name })
            })
    })
}

const colourCell = (range, domain, value) => d3.scaleQuantize().domain(domain).range(range)(value)

function calculateRiskClass(riskSummary, view = 'knownRisk') {
    if (!riskSummary) return
    switch (view) {
        case 'knownRisk':
            if (riskSummary.knownSystems === 0) return 'unknownRisk'
            return styleRiskScore(0,
                riskSummary.knownSystems * riskConfig.RISK_VALUES.high,
                riskSummary.knownRisk)
        case 'includeUnknownRisk':
            return styleRiskScore(0,
                (riskSummary.knownSystems + riskSummary.unknownSystems) * riskConfig.RISK_VALUES.high,
                riskSummary.knownRisk + riskSummary.unknownRisk)
        case 'unknowns':
            return colourCell(
                ['blue1', 'blue2', 'blue3', 'blue4', 'blue5'],
                [0, riskSummary.knownSystems + riskSummary.unknownSystems],
                riskSummary.knownSystems)
        default: throw new Error("Unknown view")
    }
}

function renderXAxis(riskLenses, panelWidth) {
    d3.select("#grid").selectAll(".riskLabel")
        .data(riskLenses)
        .enter()
        .append("g")
        .attr("transform", (d, i) => `translate(${((i * panelWidth) + panelWidth / 2) - 10}, -8), rotate(-55, 0, 0)`)
        .append("text")
        .attr("font-weight", "bold")
        .text(d => toTitle(d))
}

function renderYAxis(riskData, panelHeight) {
    d3.select("#grid").selectAll(".rootRow")
        .data(riskData)
        .append("text")
        .attr("class", "rootLabel")
        .attr("transform", `translate(-10, ${(panelHeight / 2) + 3})`)
        .attr("text-anchor", "end")
        .attr("font-weight", "bold")
        .text(d => d.key)
        .call(wrap, 100)
}

function wrap(elements, width) {
  elements.each(function() {
    const text = d3.select(this)
    const words = text.text().split(/\s+/)
    var line = []
    var lineCount = 0
    const lineHeight = 15
    const y = text.attr("y")
    var tspan = text.text(null).append("tspan")

    //FIXME: Workaround for getComputedTextLength not existing under test
    if (!tspan.node().getComputedTextLength) {
      return 
    }

    words.forEach(word => {
      line.push(word)
      tspan.text(line.join(" "))
      if (tspan.node().getComputedTextLength() > width) {
        line.pop()
        tspan.text(line.join(" "))
        line = [word]
        tspan = text.append("tspan").text(word)
        lineCount++;
      }
    })
    text.selectAll("tspan")
      .attr("x", 0)
      .attr("y", (_, i) => y + (i * lineHeight))
      .attr("dy", -(lineCount/2 * lineHeight))
  })
}

export default SystemsHeatmapVis
