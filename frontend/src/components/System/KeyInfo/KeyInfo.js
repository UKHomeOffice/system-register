import React from 'react'

const KeyInfo = (props) => {
    if (!props.info || props.info.toLowerCase() === 'unknown')
        return <strong className="highRisk">Unknown</strong>
    return props.info
}

export default KeyInfo
