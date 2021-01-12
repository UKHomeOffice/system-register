import React, { useRef } from 'react'
import Link from '../Linking/Link'
import './Menu.css'
import config from "../../config/config";
import ExternalLink from '../Linking/ExternalLink/ExternalLink';

const Menu = () => {
    return (
        <nav className="Menu">
            <ul className="centerContent">
                <li><Link exact to="/" activeClassName="selected">System Register</Link></li>
                <li><Link ref={useRef()} to="/risk_dashboard" activeClassName="selected">Risk Dashboard</Link></li>
                <li><ExternalLink href={`${config.api.url}/systems`}>API</ExternalLink></li>
                <li><Link to="/about" activeClassName="selected">About</Link></li>
                <li><Link to="/contact" activeClassName="selected">Contact</Link></li>
            </ul>
        </nav>
    )
}

export default Menu