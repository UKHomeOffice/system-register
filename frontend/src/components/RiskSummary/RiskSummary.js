import React, { useEffect } from 'react'
import * as d3 from 'd3'
import { summariseRisk } from '../../utilities/riskCalculator'
import { Paper } from '@material-ui/core'
import './RiskSummary.css'
import toLower from '../../utilities/toLower'

const WIDTH = 580
const HEIGHT = 200

const RiskSummary = (props) => {
    useEffect(() => renderVis(props.systems, props.riskLens, props.root), [props.systems, props.riskLens, props.root])
    return (
       <Paper>
          <svg id="summaryVis" width={WIDTH} height={HEIGHT} />
       </Paper>
    )
}

function renderVis(systems, riskLens, root) {
    const lenseData = buildLenseData(systems, riskLens)
    if (lenseData) {
        renderBarGraph(lenseData, root)
    }
}

function buildLenseData(systems, lenseName) {
    const lenses = summariseRisk(systems)
    const lense  = lenses.find(s => s.name === lenseName)
    if (!lense) {
      return
    }
    const riskCounts = [
      { "label": "Unknown", count: lense.unknownSystems, class: "unknownRisk" },
      { "label": "Low Risk", count: lense.lowRiskSystems, class: "lowRisk" },
      { "label": "Medium Risk", count: lense.mediumRiskSystems, class: "mediumRisk" },
      { "label": "High Risk", count: lense.highRiskSystems, class: "highRisk" }
    ]
    return {
      name: toLower(lenseName),
      riskCounts: riskCounts,
      totalSystems: riskCounts.map(c => c.count).reduce((a, b) => a + b, 0)
    }
}

function renderBarGraph(lenseData, root) {

    const barWidth = 50
    const barPadding = 30
    const topPadding = 60
    const bottomPadding = 35
    const textPadding = 20;
    const leftPadding = 0;
    const numBars = lenseData.riskCounts.length


    const barAreaHeight = HEIGHT - topPadding - bottomPadding
    const barAreaWidth = (barWidth * numBars) + (barPadding * numBars - 1)
    const barAreaOrigin = {
        x: (WIDTH / 2 - barAreaWidth / 2 + (barWidth + barPadding) / 2) + leftPadding,
        y: topPadding
    }

    const yRamp = d3.scaleLinear().domain([0, lenseData.totalSystems]).range([0, barAreaHeight])

    const vis = d3.select("#summaryVis")
    vis.selectAll(".containerG").remove()
    vis.selectAll("#riskSummaryTitle").remove()
    vis.selectAll("#noSystemsMessage").remove()

    if (lenseData.totalSystems === 0) {
        vis.append("text")
            .attr("id", 'noSystemsMessage')
            .html("No applicable systems")
            .style("font-size", 30)
            .style('text-anchor', 'middle')
            .attr("transform", `translate(${WIDTH / 2}, ${HEIGHT / 2})`)
        return
    }

    const container = vis.append('g')
        .attr('class', 'containerG')
        .attr('transform', `translate(${barAreaOrigin.x}, ${barAreaOrigin.y})`)

    vis
        .append('text')
        .attr('id', 'riskSummaryTitle')
        .html(`${root} systems by ${lenseData.name} risk level`)
        .style("text-anchor", "middle")
        .attr('transform', `translate(${WIDTH / 2}, 28)`)

    const barGroup = container.selectAll(".systemCountG")
        .data(lenseData.riskCounts)
        .enter()
        .append('g')
        .attr('class', 'systemCountG')
        .attr('transform', (d, i) => `translate(${i * (barWidth + barPadding)}, ${barAreaHeight})`)

    barGroup
        .append('rect')
        .attr('width', barWidth)
        .attr('transform', (d, i) => `translate(-${barWidth / 2}, -${yRamp(d.count)})`)
        .attr('height', d => yRamp(d.count))
        .attr('class', d => d.class)

    barGroup
        .append('text')
        .html(d => d.label)
        .attr('transform', `translate(0, ${textPadding})`)
        .style("text-anchor", "middle")
        .style("font-size", "0.9em")

    barGroup
        .append('text')
        .html(d => d.count)
        .style("text-anchor", "middle")
        .attr('transform', d => `translate(0, ${-yRamp(d.count) - 5})`)
}

export default RiskSummary
