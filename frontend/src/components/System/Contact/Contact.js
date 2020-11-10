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
    var [isEditing, setIsEditing] = useState(false)
    var [showEditIcon, setShowEditIcon] = useState(false)

    const mouseEnter = () => setShowEditIcon(true)
    const mouseLeave = () => setShowEditIcon(false)
    const cancelEdit = () => {
        setIsEditing(false)
        mouseLeave()
    }
    const saveEdit = () => {
        setIsEditing(false)
        mouseLeave()
    }

    let info = () => {
        if (isEditing === false) {
            if (showEditIcon) {
                return <span className="editMe">
                    { props.type}: <KeyInfo info={props.name} />
                    <EditIcon onClick={() => setIsEditing(true)} className="editIcon" color="disabled" fontSize="small" />
                </span>
            }
            else{
                return <>
                    { props.type}: <KeyInfo info={props.name} />
                </>
            }
        }
        else {
            return <>
                {props.type}:
                <span className="editBox">
                    <Input defaultValue={props.name} id="input-with-icon-adornment"
                        startAdornment={
                            <InputAdornment position="start">
                                <AccountCircle />
                            </InputAdornment>
                        }
                    />
                </span>
                <span className="checkIcon"><CheckBoxRoundedIcon onClick={saveEdit} fontSize="small" /></span>
                <span className="cancelIcon" ><CancelIcon onClick={cancelEdit} fontSize="small" /></span>
            </>
        }
    }

    return (
        <span className="contact" onMouseEnter={mouseEnter} onMouseLeave={mouseLeave}>
            {info()}
        </span>
    )
}

export default Contact