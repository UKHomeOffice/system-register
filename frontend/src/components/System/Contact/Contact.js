import React from 'react'
import './Contact.css'
import KeyInfo from '../KeyInfo/KeyInfo'


const Contact = (props) => {
    return (
        <span className="contact">
            { props.type}: <KeyInfo info={props.name} />
        </span>
    )
}

export default Contact