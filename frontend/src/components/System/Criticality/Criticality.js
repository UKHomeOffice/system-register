import React from 'react'
import './Criticality.css'

const Criticality = (props) => {
    switch (props.level) {
        case 'cni':
            return <span className="badge criticality-cni">CNI</span>
        case 'high':
            return <span className="badge criticality-high">HIGH</span>
        case 'medium':
            return <span className="badge criticality-medium">MEDIUM</span>
        case 'low':
            return <span className="badge criticality-low">LOW</span>
        default:
            return <span className="badge criticality-unknown">UNKNOWN</span>
    }
}


export default Criticality
