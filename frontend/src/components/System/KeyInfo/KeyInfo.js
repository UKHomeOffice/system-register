import React from 'react'
import "./KeyInfo.css"

const KeyInfo = (props) => {
    if (!props.info || props.info.toLowerCase() === 'unknown')
        return <strong className="unknownKeyInfo">UNKNOWN</strong>
    return props.info
}

export default KeyInfo
