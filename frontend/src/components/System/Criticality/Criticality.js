import React from 'react'
import './Criticality.css'

const Criticality = (props) => {
    const prefix = props.hideLabel ? "": "Criticality "
    switch (props.level) {
        case 'cni':
            return <span className="badge criticalityCNI">{prefix}CNI</span>
        case 'high':
            return <span className="badge criticalityHigh">{prefix}High</span>
        case 'medium':
            return <span className="badge criticalityMedium">{prefix}Medium</span>
        case 'low':
            return <span className="badge criticalityLow">{prefix}Low</span>
        default:
            return <span className="badge criticalityUnknown">{prefix}Unknown</span>
    }
}


export default Criticality
