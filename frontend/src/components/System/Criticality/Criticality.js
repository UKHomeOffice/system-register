import React from 'react'
import './Criticality.css'

const Criticality = (props) => {
    switch (props.level) {
        case 'cni':
            return <span className="badge criticalityCNI">CNI</span>
        case 'high':
            return <span className="badge criticalityHigh">HIGH</span>
        case 'medium':
            return <span className="badge criticalityMedium">MEDIUM</span>
        case 'low':
            return <span className="badge criticalityLow">LOW</span>
        default:
            return <span className="badge criticalityUnknown">UNKNOWN</span>
    }
}


export default Criticality
