import React from 'react'
import './SystemCard.css'
import { Link } from 'react-router-dom'
import ModifiedOn from '../ModifiedDetails/ModifiedDetails'

const trimDescription = (str) => {
    if (!str) {
        return "No Description"
    }
    if (str.length > 200) {
        return str.substring(0, 200) + '...'
    }
    return str
}

const SystemCard = (props) => {
    const { id, name, portfolio, description, last_updated } = props.system
    return (
        <div className="systemCard topMargin">
            {/* todo tests */}
            <Link to={`/system/${id}`}>
                <strong>{name}</strong>
                <small> (Portfolio: {portfolio}, <ModifiedOn date={last_updated} />)
                </small>
                <div style={{ marginTop: "3px" }}>
                    <small>{trimDescription(description)}</small>
                </div>
            </Link>
        </div>
    )
}

export default SystemCard
