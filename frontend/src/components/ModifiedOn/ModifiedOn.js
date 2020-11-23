import React from 'react'

const formatDate = (str) => {
    if (str) {
        const options = { year: 'numeric', month: 'long' }
        return new Date(str).toLocaleDateString('en-US', options)
    }

    return <strong className="highRisk">Never</strong>
}

const ModifiedOn = (props) => <span>Last modified: {formatDate(props.date)}</span>

export default ModifiedOn
