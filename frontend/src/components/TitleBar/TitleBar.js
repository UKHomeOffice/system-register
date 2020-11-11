import React from 'react'
import Auth from '../Auth/Auth'
import './TitleBar.css'
import TopNav, { asNavLinkAnchor } from '@govuk-react/top-nav';

const NavAnchor = asNavLinkAnchor('a');


const TitleBar = () => {

    return (
        <TopNav data-testid="title-bar-div" company="" serviceTitle="System Register" active={0}></TopNav>
        // <div data-testid="title-bar-div" className="title bottomMargin">
        //     <h2 className="title">System Register</h2>
        //     <Auth />
        // </div>
    )
}

export default TitleBar