import React from 'react'

const formatDate = (str) => {
    if (str) {
        const options = { year: 'numeric', month: 'long', day: 'numeric' }
        return new Date(str).toLocaleDateString('en-GB', options)
    }

    return <span>Never</span>
}

const ModifiedOn = (props) => <span data-testid='modified-on'>Last modified: {formatDate(props.date)}</span>

export default ModifiedOn
