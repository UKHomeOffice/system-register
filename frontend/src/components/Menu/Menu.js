import React from 'react'
import { NavLink } from 'react-router-dom'
import './Menu.css'

const Menu = () => {
    return (
        <nav className="Menu">
            <ul className="centerContent">
                <li><NavLink exact to="/" activeClassName="selected">System Register</NavLink></li>
                <li><NavLink to="/risk_dashboard" activeClassName="selected">Risk Dashboard</NavLink></li>
                <li><NavLink to="/about" activeClassName="selected">About</NavLink></li>
                <li><NavLink to="/contact" activeClassName="selected">Contact</NavLink></li>
            </ul>
        </nav>
    )
}

export default Menu