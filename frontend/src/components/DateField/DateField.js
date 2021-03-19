import React from "react";
import PropTypes from "prop-types";

import "./DateField.css";
import TextField from "../TextField";

function DateField({ children, hintText, name }) {
  return (
    <div>
      <h3 className="date-field-primary">{children}</h3>
      <p className="date-field-secondary">{hintText}</p>

      <div className="date-field-date-container">
        <TextField name={name + ".day"} inputClassName="date-field-day-field">
          Day
        </TextField>
        <TextField
          name={name + ".month"}
          inputClassName="date-field-month-field"
        >
          Month
        </TextField>
        <TextField name={name + ".year"} inputClassName="date-field-year-field">
          Year
        </TextField>
      </div>
    </div>
  );
}

export default DateField;

DateField.propTypes = {
  children: PropTypes.string,
  hintText: PropTypes.string,
  name: PropTypes.string,
};
