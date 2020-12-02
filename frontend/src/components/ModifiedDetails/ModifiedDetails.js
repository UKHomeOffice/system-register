import React from 'react'
import './ModifiedDetails.css'


const formatDate = (str) => {
    if (str) {
        const options = { year: 'numeric', month: 'long', day: 'numeric' }
        return new Date(str).toLocaleDateString('en-GB', options)
    }

    return <span>Never</span>
}

const formatAuthor = (str) => {
    return str? " by " + str : "";
}

const ModifiedDetails = (props) => <span className="message" data-testid='modified-on'>Last modified: {formatDate(props.date)}{formatAuthor(props.author_name)}</span>

export default ModifiedDetails
