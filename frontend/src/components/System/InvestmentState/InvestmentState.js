import React from 'react'
import './InvestmentState.css'

const InvestmentState = (props) => {
    switch (props.state) {
        case 'evergreen':
            return <span className="badge investEvergreen">EVERGREEN</span>
        case 'invest':
            return <span className="badge invest">INVEST</span>
        case 'maintain':
            return <span className="badge investMaintain">MAINTAIN</span>
        case 'sunset':
            return <span className="badge investSunset">SUNSET</span>
        case 'decommissioned':
            return <span className="badge investDecommissioned">DECOMMISSIONED</span>
        case 'cancelled':
            return <span className="badge investCancelled">CANCELLED</span>
        default:
            return <span className="badge investmentUnknown">UNKNOWN</span>
    }
}

export default InvestmentState