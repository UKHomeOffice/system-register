import React from 'react'
import './SystemCard.css'
import Link from '../Linking/Link'
import ModifiedDetails from '../ModifiedDetails/ModifiedDetails'

const trimDescription = (str) => {
    if (!str) {
        return "No Description"
    }
    if (str.length > 200) {
        return str.substring(0, 200) + '...'
    }
    return str
}

const SystemCard = (props) => { //todo code review, extract to reusable styles "LINK" component
    const { id, name, portfolio, description, last_updated } = props.system
    return (
        <div className="systemCard">
            <Link style={{color: "black"}} className="systemCard-link" to={`/system/${id}`}>
                <strong>{name}</strong>
                <small> (Portfolio: {portfolio}, <ModifiedDetails date={last_updated.timestamp} author_name={last_updated.author_name} />)
                </small>
                <div>
                    <small>{trimDescription(description)}</small>
                </div>
            </Link>
        </div>
    )
}

export default SystemCard
