import React from 'react'
import Auth from '../Auth/Auth' 
import './TitleBar.css'

const TitleBar = () => {
    return (
        <div data-testid="title-bar-div" className="title bottomMargin">
                <h2 className="title">System Register</h2>
           <Auth />
        </div>
    )
}

export default TitleBar