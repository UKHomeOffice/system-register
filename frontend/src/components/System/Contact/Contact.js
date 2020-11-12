import React, { useState } from 'react'
import './Contact.css'
import KeyInfo from '../KeyInfo/KeyInfo'
import EditIcon from '@material-ui/icons/Edit';
import CheckBoxRoundedIcon from '@material-ui/icons/CheckBoxRounded';
import CancelIcon from '@material-ui/icons/Cancel';

import Input from '@material-ui/core/Input';
import InputAdornment from '@material-ui/core/InputAdornment';
import AccountCircle from '@material-ui/icons/AccountCircle';


const Contact = (props) => {
    return (
        <span className="contact">
            { props.type}: <KeyInfo info={props.name} />
        </span>
    )
}

export default Contact