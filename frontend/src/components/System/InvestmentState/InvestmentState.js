import React from 'react'
import './InvestmentState.css'

const InvestmentState = (props) => {
    switch (props.state) {
        case 'evergreen':
            return <span className="badge investEvergreen">Evergreen</span>
        case 'invest':
            return <span className="badge invest">Invest</span>
        case 'maintain':
            return <span className="badge investMaintain">Maintain</span>
        case 'sunset':
            return <span className="badge investSunset">Sunset</span>
        case 'decommissioned':
            return <span className="badge investDecommissioned">Decommissioned</span>
        default:
            return <span className="badge investmentUnknown">Investment Unknown</span>
    }
}

export default InvestmentState