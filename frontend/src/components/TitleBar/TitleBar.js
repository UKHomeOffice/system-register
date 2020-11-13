import React from 'react'
import Auth from '../Auth/Auth'
import './TitleBar.css'
import TopNav from '@govuk-react/top-nav'

const TitleBar = () => {

    const serviceTitle = <div className="title-bar-title">System Register</div>

    return (
        <TopNav data-testid="title-bar-div" company="" serviceTitle={serviceTitle} >
            <div className="title-bar-login">
                <Auth />
            </div>
        </TopNav>
    )
}

export default TitleBar